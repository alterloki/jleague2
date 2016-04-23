package ru.jleague13.all;

import ru.jleague13.entity.Team;

import java.util.*;

/**
 * @author ashevenkov 23.04.16 13:54.
 */
public class AllZip {

    private Date date;
    private Map<String, String> competitions;
    private int bankRate;
    private Map<String, Team> teams;
    private String currentTeam;

    public AllZip(Date date, Map<String, String> competitions, int bankRate, Map<String, Team> teams) {
        this.date = date;
        this.competitions = competitions;
        this.bankRate = bankRate;
        this.teams = teams;
        this.currentTeam = "";
    }

    public Date getDate() {
        return date;
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams.values());
    }

    public Team getCurrentTeam() {
        return teams.get(currentTeam);
    }

    public void setCurrentTeam(String faId) {
        this.currentTeam = faId;
    }

    public Map<String, String> getCompetitions() {
        return competitions;
    }

    public Team getTeamById(String faId) {
        return teams.get(faId);
    }

    public int getBankRate() {
        return bankRate;
    }
}
