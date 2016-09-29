package ru.jleague13.entity;

import java.util.Date;

/**
 * @author ashevenkov 29.09.16 10:08.
 */
public class PlayerGameInfo {

    private int number;
    private int fitness;
    private int morale;
    private int disqualification;
    private int rest;
    private int teamwork;
    private int games;
    private int goalsTotal;
    private int goalsMissed;
    private int goalsChamp;
    private double mark;
    private int gamesCareer;
    private int goalsCareer;
    private int yellowCards;
    private int redCards;
    private boolean transfer;
    private boolean lease;
    private String birthplace;
    private Date birthdate;
    private int assists;
    private int profit;

    public PlayerGameInfo(int number, int fitness, int morale, int disqualification,
                          int rest, int teamwork, int games, int goalsTotal, int goalsMissed,
                          int goalsChamp, double mark, int gamesCareer, int goalsCareer,
                          int yellowCards, int redCards, boolean transfer, boolean lease,
                          String birthplace, Date birthdate, int assists, int profit) {
        this.number = number;
        this.fitness = fitness;
        this.morale = morale;
        this.disqualification = disqualification;
        this.rest = rest;
        this.teamwork = teamwork;
        this.games = games;
        this.goalsTotal = goalsTotal;
        this.goalsMissed = goalsMissed;
        this.goalsChamp = goalsChamp;
        this.mark = mark;
        this.gamesCareer = gamesCareer;
        this.goalsCareer = goalsCareer;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.transfer = transfer;
        this.lease = lease;
        this.birthplace = birthplace;
        this.birthdate = birthdate;
        this.assists = assists;
        this.profit = profit;
    }

    public int getNumber() {
        return number;
    }

    public int getFitness() {
        return fitness;
    }

    public int getMorale() {
        return morale;
    }

    public int getDisqualification() {
        return disqualification;
    }

    public int getRest() {
        return rest;
    }

    public int getTeamwork() {
        return teamwork;
    }

    public int getGames() {
        return games;
    }

    public int getGoalsTotal() {
        return goalsTotal;
    }

    public int getGoalsMissed() {
        return goalsMissed;
    }

    public int getGoalsChamp() {
        return goalsChamp;
    }

    public double getMark() {
        return mark;
    }

    public int getGamesCareer() {
        return gamesCareer;
    }

    public int getGoalsCareer() {
        return goalsCareer;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public boolean isLease() {
        return lease;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public int getAssists() {
        return assists;
    }

    public int getProfit() {
        return profit;
    }
}
