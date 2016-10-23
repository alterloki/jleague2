package ru.jleague13.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ashevenkov 07.10.16 10:20.
 */
public class Calendar {

    private Map<Date, CalendarDay> days = new TreeMap<>();

    public Calendar() {
    }

    public Calendar(Map<Date, CalendarDay> days) {
        this.days = days;
    }

    public Map<Date, CalendarDay> getDays() {
        return days;
    }

    public void addDay(CalendarDay day) {
        days.put(day.getDate(), day);
    }

    public void addEvent(Event event) {
        CalendarDay calendarDay = days.get(event.getDay());
        if(calendarDay == null) {
            calendarDay = new CalendarDay(event.getDay());
            days.put(event.getDay(), calendarDay);
        }
        calendarDay.addEvent(event);
    }
}
