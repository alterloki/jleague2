package ru.jleague13.calendar;

import java.util.Date;

/**
 * @author ashevenkov 07.10.16 10:22.
 */
public class Event {

    private EventType eventType = EventType.NONE;
    private EventType scheduleFor = EventType.NONE;
    private String text;
    private int tourNum;
    private int part;
    private MatchType matchType;
    private Date day;

    public Event(Date day) {
        this.day = day;
    }

    public Event(EventType eventType, String text, int tourNum, int part) {
        this.eventType = eventType;
        this.text = text;
        this.tourNum = tourNum;
        this.part = part;
        this.matchType = MatchType.NONE;
    }

    public Event(EventType eventType, String text, int tourNum, int part, MatchType matchType) {
        this.eventType = eventType;
        this.text = text;
        this.tourNum = tourNum;
        this.part = part;
        this.matchType = matchType;
    }

    public EventType getScheduleFor() {
        return scheduleFor;
    }

    public void setScheduleFor(EventType scheduleFor) {
        this.scheduleFor = scheduleFor;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getText() {
        return text;
    }

    public int getTourNum() {
        return tourNum;
    }

    public int getPart() {
        return part;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setTourNum(int tourNum) {
        this.tourNum = tourNum;
    }

    @Override
    public String toString() {
        return "Event{" +
                "=" + eventType +
                "=" + scheduleFor +
                "='" + text + '\'' +
                "=" + tourNum +
                "=" + part +
                "=" + matchType +
                '}';
    }
}
