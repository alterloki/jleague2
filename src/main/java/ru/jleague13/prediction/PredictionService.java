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

    public List<PredictionUser> getPredictionUsersTable() {
        ArrayList<PredictionUser> predictionUsers = new ArrayList<>();
        predictionUsers.add(new PredictionUser("alterloki", 15, 1));
        predictionUsers.add(new PredictionUser("Def", 10, 2));
        return predictionUsers;
    }

    public List<Match> getPredictionMatchesForUser(EventType eventType) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
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


}
