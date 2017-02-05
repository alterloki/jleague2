package ru.jleague13.repository;

import ru.jleague13.calendar.Calendar;
import ru.jleague13.calendar.CalendarDay;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ashevenkov 15.10.16 16:28.
 */
public interface CalendarEventsDao {

    List<Event> loadCalendarEvents(Date from, Date to);

    List<Event> loadCalendarEventsOfType(Date from, Date to, EventType eventType);

    void saveCalendarEvents(List<Event> calendarEvents);

    void saveCalendarEvents(Map<Date, CalendarDay> calendarEvents);

    List<Event> getDayEvents(Date day);
}
