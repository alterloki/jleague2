package ru.jleague13.prediction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.jleague13.calendar.CalendarDay;
import ru.jleague13.calendar.CalendarManager;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Match;
import ru.jleague13.entity.Player;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.*;
import ru.jleague13.security.SecurityService;
import ru.jleague13.service.SeasonService;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 25.09.15 23:00.
 */
@Component
public class PredictionService {

    private Log log = LogFactory.getLog(PredictionService.class);

    @Autowired
    private CalendarManager calendarManager;
    @Autowired
    private MatchDao matchDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PredictionDao predictionDao;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private SeasonService seasonService;
    @Autowired
    private CalendarEventsDao calendarEventsDao;
    @Autowired
    private TeamDao teamDao;

    public List<PredictionUser> getPredictionUsersTable() {
        List<PredictionUser> predictionUsers = predictionDao.loadPredictionUsers();
        for(int i = 0; i < predictionUsers.size(); i++) {
            predictionUsers.get(i).setPosition(i + 1);
        }
        return predictionUsers;
    }

    public void updateScores() {
        Country japan = countryDao.getCountriesMap().get("Япония");
        Date seasonStart = seasonService.getSeasonStart();
        List<Event> regularEvents = calendarEventsDao.loadCalendarEventsOfType(
                seasonStart, new Date(),
                EventType.REGULAR_TOUR);
        Map<Integer, PredictionUser> userMap = new HashMap<>();
        for (Event regularEvent : regularEvents) {
            List<Match> matches = matchDao.loadMatches(regularEvent, japan.getId());
            for (Match match : matches) {
                List<Prediction> predictions =
                        predictionDao.loadMatchPredictions(match.getId());
                if (predictions.size() > 0) {
                    for (Prediction prediction : predictions) {
                        if(prediction.getGuestScore() >= 0 && prediction.getOwnerScore() >= 0) {
                            if(prediction.getGuestScore() == match.getGuestScore() &&
                                    prediction.getOwnerScore() == match.getOwnerScore()) {
                                addScore(userMap, prediction.getUserId(), 4);
                            } else if(prediction.getOwnerScore() - prediction.getGuestScore() ==
                                    match.getOwnerScore() - match.getGuestScore()) {
                                addScore(userMap, prediction.getUserId(), 2);
                            } else if(getWinIndex(prediction.getOwnerScore(), prediction.getGuestScore()) ==
                                    getWinIndex(match.getOwnerScore(), match.getGuestScore())) {
                                addScore(userMap, prediction.getUserId(), 1);
                            }
                        }
                    }
                }
            }
        }
        for (Map.Entry<Integer, PredictionUser> userPredictionEntry : userMap.entrySet()) {
            log.info("User id = " + userPredictionEntry.getKey() + " points = " +
                    userPredictionEntry.getValue().getPoints());
            predictionDao.savePoints(userPredictionEntry.getKey(),
                    userPredictionEntry.getValue().getPoints());
        }
    }

    private int getWinIndex(int ownerScore, int guestScore) {
        return ownerScore > guestScore ? 1 : (ownerScore < guestScore ? -1 : 0);
    }

    private void addScore(Map<Integer, PredictionUser> userMap, int userId, int score) {
        PredictionUser predictionUser = userMap.get(userId);
        if(predictionUser == null) {
            predictionUser = new PredictionUser(userId, "", 0, 0);
            userMap.put(userId, predictionUser);
        }
        predictionUser.setPoints(predictionUser.getPoints() + score);
    }

    public List<List<Match>> getPredictionMatchesForUser(EventType eventType) {
        Country japan = countryDao.getCountriesMap().get("Япония");
        String name = securityService.findLoggedInUsername();
        ru.jleague13.entity.User faUser = userDao.getUserByLogin(name);
        java.util.Calendar calendarInst = new GregorianCalendar();
        calendarInst.add(Calendar.HOUR_OF_DAY, 3);
        CalendarManager.trunc(calendarInst);
        for(int i = 0; i < 10; i++) {
            calendarInst.add(Calendar.DAY_OF_MONTH, 1);
            CalendarDay calendarDay = calendarManager.getCalendarDay(calendarInst.getTime());
            Set<Event> events = calendarDay.getEvents();
            for (Event event : events) {
                if(event.getEventType() == eventType) {
                    List<Match> matches = matchDao.loadMatches(event, japan.getId());
                    if(faUser != null) {
                        for (Match match : matches) {
                            Prediction prediction =
                                    predictionDao.getUserMatchPrediction(faUser.getId(), match.getId());
                            if (prediction != null) {
                                match.setGuestScore(prediction.getGuestScore());
                                match.setOwnerScore(prediction.getOwnerScore());
                            }
                        }
                    }
                    List<Team> countryTeams = teamDao.getCountryTeams(japan.getId());
                    Map<Integer, Team> id2teams = countryTeams.stream().collect(
                            Collectors.toMap(Team::getId, Function.identity()));
                    ArrayList<List<Match>> result = new ArrayList<>();
                    for(int j = 0; j < 3; j++) {
                        result.add(new ArrayList<>());
                    }
                    for (Match match : matches) {
                        Team team = id2teams.get(match.getOwnerTeamId());
                        result.get(team.getDiv() - 1).add(match);
                    }
                    return result;
                }
            }
        }
        return new ArrayList<>();
    }


    public void savePrediction(List<Match> matches) {
        String name = securityService.findLoggedInUsername();
        ru.jleague13.entity.User faUser = userDao.getUserByLogin(name);
        if(faUser != null) {
            if(!predictionDao.isUserParticipant(faUser.getId())) {
                predictionDao.savePoints(faUser.getId(), 0);
            }
            java.util.Calendar calendarInst = new GregorianCalendar();
            calendarInst.add(Calendar.HOUR_OF_DAY, 3);
            for (Match match : matches) {
                Date matchDate = matchDao.loadMatchById(match.getId()).getMatchEvent().getDay();
                if(matchDate.after(calendarInst.getTime())) {
                    predictionDao.savePrediction(new Prediction(faUser.getId(), match.getId(),
                            match.getOwnerScore(), match.getGuestScore(), new Date()));
                }
            }
        }
    }

    public List<PredictionLog> predictionLog() {
        return null;
    }
}
