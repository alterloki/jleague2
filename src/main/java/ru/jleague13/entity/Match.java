package ru.jleague13.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;

import java.util.Objects;

/**
 * @author ashevenkov 28.01.17 15:54.
 */
public class Match {

    private Log log = LogFactory.getLog(Match.class);

    private int id;
    private int ownerTeamId;
    private String ownerTeamName;
    private int guestTeamId;
    private String guestTeamName;

    private int ownerScore;
    private int guestScore;

    private Event matchEvent;

    public Match() {

    }

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

    public String getOwnerScoreS() {
        return ownerScore < 0 ? "?" : Integer.toString(ownerScore);
    }

    public void setOwnerScoreS(String ownerScoreString) {
        try {
            this.ownerScore = Integer.parseInt(ownerScoreString);
        } catch (NumberFormatException e) {
            this.ownerScore = -1;
        }
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
    }

    public String getGuestScoreS() {
        return guestScore < 0 ? "?" : Integer.toString(guestScore);
    }

    public void setGuestScoreS(String guestScoreString) {
        try {
            this.guestScore = Integer.parseInt(guestScoreString);
        } catch (NumberFormatException e) {
            this.guestScore = -1;
        }
    }

    public Event getMatchEvent() {
        return matchEvent;
    }

    public void setMatchEvent(Event matchEvent) {
        this.matchEvent = matchEvent;
    }

    public String getScore() {
        String os = ownerScore < 0 ? "?" : Integer.toString(ownerScore);
        String gs = guestScore < 0 ? "?" : Integer.toString(guestScore);
        return os + ":" + gs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(ownerTeamId, match.ownerTeamId) &&
                Objects.equals(guestTeamId, match.guestTeamId) &&
                Objects.equals(ownerScore, match.ownerScore) &&
                Objects.equals(guestScore, match.guestScore) &&
                Objects.equals(matchEvent.getDay(), match.matchEvent.getDay()) &&
                Objects.equals(matchEvent.getEventType(), match.matchEvent.getEventType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerTeamId, guestTeamId, ownerScore, guestScore, matchEvent);
    }
}
