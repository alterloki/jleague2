package ru.jleague13.entity;

/**
 * @author ashevenkov 26.04.16 16:45.
 */
public class Abilities {

    private int shooting;
    private int handling;
    private int reflexes;
    private int passing;
    private int cross;
    private int dribbling;
    private int tackling;
    private int heading;
    private int speed;
    private int stamina;

    public Abilities() {
    }

    public Abilities(int shooting, int handling, int reflexes, int passing,
                     int cross, int dribbling, int tackling, int heading,
                     int speed, int stamina) {
        this.shooting = shooting;
        this.handling = handling;
        this.reflexes = reflexes;
        this.passing = passing;
        this.cross = cross;
        this.dribbling = dribbling;
        this.tackling = tackling;
        this.heading = heading;
        this.speed = speed;
        this.stamina = stamina;
    }

    public int getShooting() {
        return shooting;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public int getHandling() {
        return handling;
    }

    public void setHandling(int handling) {
        this.handling = handling;
    }

    public int getReflexes() {
        return reflexes;
    }

    public void setReflexes(int reflexes) {
        this.reflexes = reflexes;
    }

    public int getPassing() {
        return passing;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }

    public int getCross() {
        return cross;
    }

    public void setCross(int cross) {
        this.cross = cross;
    }

    public int getDribbling() {
        return dribbling;
    }

    public void setDribbling(int dribbling) {
        this.dribbling = dribbling;
    }

    public int getTackling() {
        return tackling;
    }

    public void setTackling(int tackling) {
        this.tackling = tackling;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
}
