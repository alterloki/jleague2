package ru.jleague13.prediction;

import java.util.Date;

/**
 * @author ashevenkov 28.01.17 16:22.
 */
public class Prediction {
    private int userId;
    private int matchId;
    private int ownerScore;
    private int guestScore;
    private Date predictionTime;

    public Prediction(int userId, int matchId, int ownerScore, int guestScore, Date predictionTime) {
        this.userId = userId;
        this.matchId = matchId;
        this.ownerScore = ownerScore;
        this.guestScore = guestScore;
        this.predictionTime = predictionTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
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

    public Date getPredictionTime() {
        return predictionTime;
    }

    public void setPredictionTime(Date predictionTime) {
        this.predictionTime = predictionTime;
    }
}
