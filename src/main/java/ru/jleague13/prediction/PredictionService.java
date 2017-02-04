package ru.jleague13.prediction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.jleague13.calendar.CalendarDay;
import ru.jleague13.calendar.CalendarManager;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Match;
import ru.jleague13.entity.Player;
import ru.jleague13.repository.MatchDao;
import ru.jleague13.repository.UserDao;
import ru.jleague13.security.SecurityService;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;

/**
 * @author ashevenkov 25.09.15 23:00.
 */
@Component
public class PredictionService {

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


    public List<PredictionUser> getPredictionUsersTable() {
        List<PredictionUser> predictionUsers = predictionDao.loadPredictionUsers();
        for(int i = 0; i < predictionUsers.size(); i++) {
            predictionUsers.get(i).setPosition(i + 1);
        }
        return predictionUsers;
    }

    public List<Match> getPredictionMatchesForUser(EventType eventType) {
        String name = securityService.findLoggedInUsername();
        ru.jleague13.entity.User faUser = userDao.getUserByLogin(name);
        java.util.Calendar calendarInst = new GregorianCalendar();
        CalendarManager.trunc(calendarInst);
        for(int i = 0; i < 10; i++) {
            calendarInst.add(Calendar.DAY_OF_MONTH, 1);
            CalendarDay calendarDay = calendarManager.getCalendarDay(calendarInst.getTime());
            Set<Event> events = calendarDay.getEvents();
            for (Event event : events) {
                if(event.getEventType() == eventType) {
                    List<Match> matches = matchDao.loadMatches(event);
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
                    return matches;
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
            for (Match match : matches) {
                predictionDao.savePrediction(new Prediction(faUser.getId(), match.getId(),
                        match.getOwnerScore(), match.getGuestScore(), new Date()));
            }
        }
    }
}
