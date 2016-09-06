package ru.jleague13.entity;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Set;

/**
 * @author ashevenkov 10.09.15 23:36.
 */
public class Team {

    private int id;
    private String shortName;
    private String name;
    private int countryId;
    private int managerId = 0;
    private int div;
    private String emblem;
    private TeamInfo teamInfo;
    private List<Player> players;
    private String managerLogin;

    public  Team() {
    }

    public Team(int id, String shortName, String name, int countryId) {
        this(id, shortName, name, countryId, 0, 0, null);
    }

    public Team(int id, String shortName, String name, int countryId, int managerId, int div, TeamInfo teamInfo) {
        this.id = id;
        this.shortName = shortName;
        this.name = name;
        this.countryId = countryId;
        this.managerId = managerId;
        this.div = div;
        this.teamInfo = teamInfo;
    }

    public String getManagerLogin() {
        return managerLogin;
    }

    public void setManagerLogin(String managerLogin) {
        this.managerLogin = managerLogin;
    }

    public Team(int countryId) {
        this.countryId = countryId;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getEmblem() {
        return emblem;
    }

    public void setEmblem(String emblem) {
        this.emblem = emblem;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getDiv() {
        return div;
    }

    public void setDiv(int div) {
        this.div = div;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return java.util.Objects.equals(countryId, team.countryId) &&
                java.util.Objects.equals(managerId, team.managerId) &&
                java.util.Objects.equals(div, team.div) &&
                java.util.Objects.equals(shortName, team.shortName) &&
                java.util.Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(shortName, name, countryId, managerId, div);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
