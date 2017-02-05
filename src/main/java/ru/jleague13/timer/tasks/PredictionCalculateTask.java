package ru.jleague13.timer.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.calendar.CalendarDay;
import ru.jleague13.calendar.CalendarManager;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.prediction.PredictionService;
import ru.jleague13.timer.AbstractFaTask;
import ru.jleague13.timer.ProgressConnection;

import java.util.Date;
import java.util.Set;

/**
 * @author ashevenkov 05.02.17 23:34.
 */
@Component
public class PredictionCalculateTask extends AbstractFaTask {

    private Log log = LogFactory.getLog(PredictionCalculateTask.class);

    @Autowired
    private CalendarManager calendarManager;
    @Autowired
    private PredictionService predictionService;

    public PredictionCalculateTask() {
        super("predictionCalculateTask", "0 15 2 * * *");
    }

    @Override
    public void runTask(ProgressConnection progress) throws Exception {
        log.info("Running score update task");
        /*CalendarDay calendarDay = calendarManager.getCalendarDay(new Date());
        Set<Event> events = calendarDay.getEvents();
        for (Event event : events) {
            if (event.getEventType() == EventType.REGULAR_TOUR) {*/
                predictionService.updateScores();
            /*}
        }*/
    }
}
