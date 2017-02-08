package ru.jleague13.prediction;

import java.util.Date;

/**
 * @author ashevenkov 07.02.17 0:20.
 */
public class PredictionLog {

    private int userId;
    private String userName;
    private Date time;

    public PredictionLog(int userId, String userName, Date time) {
        this.userId = userId;
        this.userName = userName;
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Date getTime() {
        return time;
    }
}
