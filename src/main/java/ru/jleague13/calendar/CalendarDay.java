package ru.jleague13.calendar;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ashevenkov 07.10.16 10:20.
 */
public class CalendarDay {

    private Date date;
    private Set<Event> events;

    public CalendarDay(Date date) {
        this.date = date;
        events = new HashSet<>();
    }

    public CalendarDay(Date date, Set<Event> events) {
        this.date = date;
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public Date getDate() {
        return date;
    }

    public Set<Event> getEvents() {
        return events;
    }
}
