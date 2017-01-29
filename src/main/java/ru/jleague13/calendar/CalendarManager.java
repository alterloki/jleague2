package ru.jleague13.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.repository.CalendarEventsDao;

import java.util.*;

/**
 * @author ashevenkov 15.10.16 16:27.
 */
@Component
public class CalendarManager {

    @Autowired
    private CalendarEventsDao calendarDao;

    public Calendar getMonthCalendar(Date day) {
        Calendar calendar = new Calendar();
        java.util.Calendar calendarInst = new GregorianCalendar();
        calendarInst.setTime(day);
        trunc(calendarInst);
        calendarInst.set(java.util.Calendar.DAY_OF_MONTH, 1);
        Date dayFrom = calendarInst.getTime();
        calendar.addDay(new CalendarDay(dayFrom));
        calendarInst.add(java.util.Calendar.DAY_OF_MONTH, 1);
        while(calendarInst.get(java.util.Calendar.DAY_OF_MONTH) != 1) {
            calendar.addDay(new CalendarDay(calendarInst.getTime()));
            calendarInst.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }
        Date dayTo = calendarInst.getTime();
        List<Event> events = calendarDao.loadCalendarEvents(dayFrom, dayTo);
        for (Event event : events) {
            calendar.addEvent(event);
        }
        return calendar;
    }

    public static void trunc(java.util.Calendar calendarInst) {
        calendarInst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendarInst.set(java.util.Calendar.MINUTE, 0);
        calendarInst.set(java.util.Calendar.SECOND, 0);
        calendarInst.set(java.util.Calendar.MILLISECOND, 0);
    }

    public Calendar getCurrentCalendar() {
        return getMonthCalendar(new Date());
    }

    public void saveCalendar(Calendar calendar) {
        calendarDao.saveCalendarEvents(calendar.getDays());
    }

    public CalendarDay getCalendarDay(Date day) {
        List<Event> dayEvents = calendarDao.getDayEvents(day);
        return new CalendarDay(day, new HashSet<>(dayEvents));
    }
}
