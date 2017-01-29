package ru.jleague13.prediction;

/**
 * @author ashevenkov 28.01.17 16:07.
 */
public class PredictionUser {

    private String name;
    private int points;
    private int position;

    public PredictionUser(String name, int points, int position) {
        this.name = name;
        this.points = points;
        this.position = position;
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
}
