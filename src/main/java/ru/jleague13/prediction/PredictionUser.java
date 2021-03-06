package ru.jleague13.prediction;

/**
 * @author ashevenkov 28.01.17 16:07.
 */
public class PredictionUser {

    private String name;
    private int points;
    private int position;
    private int userId;

    public PredictionUser(int userId, String name, int points, int position) {
        this.name = name;
        this.points = points;
        this.position = position;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getText() {
        return Integer.toString(position) + ". " + name + " (" + Integer.toString(points) + ")";
    }
}
