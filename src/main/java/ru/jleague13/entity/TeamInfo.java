package ru.jleague13.entity;

import java.util.List;

/**
 * @author ashevenkov 23.04.16 21:24.
 */
public class TeamInfo {

    private int teamId;
    private int games;
    private int stadiumCapacity;
    private int boom;
    private String town;
    private int teamFinance;
    private String stadium;
    private int stadiumState;
    private int rating;
    private int sportbase;
    private int sportbaseState;
    private boolean sportschool;
    private int sportschoolState;
    private int coach;
    private int goalkeepersCoach;
    private int defendersCoach;
    private int midfieldersCoach;
    private int forwardsCoach;
    private int fitnessCoach;
    private int moraleCoach;
    private int doctorQualification;
    private int doctorPlayers;
    private int scout;
    private int homeTop;
    private int awayTop;
    private int homeBottom;
    private int awayBottom;
    private List<String> competitions;

    public TeamInfo(int teamId, String town, int games, int stadiumCapacity, int boom, int teamFinance,
                    String stadium, int stadiumState, int rating, int sportbase, int sportbaseState,
                    boolean sportschool, int sportschoolState, int coach, int goalkeepersCoach,
                    int defendersCoach, int midfieldersCoach, int forwardsCoach, int fitnessCoach,
                    int moraleCoach, int doctorQualification, int doctorPlayers, int scout, int homeTop,
                    int awayTop, int homeBottom, int awayBottom, List<String> competitions) {
        this.town = town;
        this.teamId = teamId;
        this.games = games;
        this.stadiumCapacity = stadiumCapacity;
        this.boom = boom;
        this.teamFinance = teamFinance;
        this.stadium = stadium;
        this.stadiumState = stadiumState;
        this.rating = rating;
        this.sportbase = sportbase;
        this.sportbaseState = sportbaseState;
        this.sportschool = sportschool;
        this.sportschoolState = sportschoolState;
        this.coach = coach;
        this.goalkeepersCoach = goalkeepersCoach;
        this.defendersCoach = defendersCoach;
        this.midfieldersCoach = midfieldersCoach;
        this.forwardsCoach = forwardsCoach;
        this.fitnessCoach = fitnessCoach;
        this.moraleCoach = moraleCoach;
        this.doctorQualification = doctorQualification;
        this.doctorPlayers = doctorPlayers;
        this.scout = scout;
        this.homeTop = homeTop;
        this.awayTop = awayTop;
        this.homeBottom = homeBottom;
        this.awayBottom = awayBottom;
        this.competitions = competitions;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getGames() {
        return games;
    }

    public int getStadiumCapacity() {
        return stadiumCapacity;
    }

    public int getBoom() {
        return boom;
    }

    public int getTeamFinance() {
        return teamFinance;
    }

    public String getStadium() {
        return stadium;
    }

    public int getStadiumState() {
        return stadiumState;
    }

    public int getRating() {
        return rating;
    }

    public int getSportbase() {
        return sportbase;
    }

    public int getSportbaseState() {
        return sportbaseState;
    }

    public boolean isSportschool() {
        return sportschool;
    }

    public int getSportschoolState() {
        return sportschoolState;
    }

    public int getCoach() {
        return coach;
    }

    public int getGoalkeepersCoach() {
        return goalkeepersCoach;
    }

    public int getDefendersCoach() {
        return defendersCoach;
    }

    public int getMidfieldersCoach() {
        return midfieldersCoach;
    }

    public int getForwardsCoach() {
        return forwardsCoach;
    }

    public int getFitnessCoach() {
        return fitnessCoach;
    }

    public int getMoraleCoach() {
        return moraleCoach;
    }

    public int getDoctorQualification() {
        return doctorQualification;
    }

    public int getDoctorPlayers() {
        return doctorPlayers;
    }

    public int getScout() {
        return scout;
    }

    public int getHomeTop() {
        return homeTop;
    }

    public int getAwayTop() {
        return awayTop;
    }

    public int getHomeBottom() {
        return homeBottom;
    }

    public int getAwayBottom() {
        return awayBottom;
    }

    public String getTown() {
        return town;
    }

    public List<String> getCompetitions() {
        return competitions;
    }
}
