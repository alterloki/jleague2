package ru.jleague13.entity;

import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;

/**
 * @author ashevenkov 28.01.17 15:54.
 */
public class Match {
    private int id;
    private int ownerTeamId;
    private String ownerTeamName;
    private int guestTeamId;
    private String guestTeamName;

    private int ownerScore;
    private int guestScore;

    private Event matchEvent;

    public Match(int id, int ownerTeamId, String ownerTeamName, int guestTeamId,
                 String guestTeamName, int ownerScore, int guestScore, Event matchEvent) {
        this.id = id;
        this.ownerTeamId = ownerTeamId;
        this.ownerTeamName = ownerTeamName;
        this.guestTeamId = guestTeamId;
        this.guestTeamName = guestTeamName;
        this.ownerScore = ownerScore;
        this.guestScore = guestScore;
        this.matchEvent = matchEvent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerTeamId() {
        return ownerTeamId;
    }

    public void setOwnerTeamId(int ownerTeamId) {
        this.ownerTeamId = ownerTeamId;
    }

    public String getOwnerTeamName() {
        return ownerTeamName;
    }

    public void setOwnerTeamName(String ownerTeamName) {
        this.ownerTeamName = ownerTeamName;
    }

    public int getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(int guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public int getOwnerScore() {
        return ownerScore;
    }

    public void setOwnerScore(int ownerScore) {
        this.ownerScore = ownerScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public Event getMatchEvent() {
        return matchEvent;
    }

    public void setMatchEvent(Event matchEvent) {
        this.matchEvent = matchEvent;
    }
}
