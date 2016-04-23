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
    private String emblem;
    private int managerId = 0;
    private String managerLogin = "";
    private int div;

    public  Team() {
    }

    public Team(int id, String shortName, String name, int countryId) {
        this.id = id;
        this.shortName = shortName;
        this.name = name;
        this.countryId = countryId;
    }

    public Team(int countryId) {
        this.countryId = countryId;
    }

    public Team(String name, String id, String town, int countryId,
                String stadium, int managerId, String managerLogin, int games, int stadiumCapacity,
                int stadiumState, int boom, int teamFinance, int managerFinance, int rating,
                int sportbase, int sportbaseState, boolean sportschool, int sportschoolState,
                int coach, int goalkeepersCoach, int defendersCoach, int midfieldersCoach,
                int forwardsCoach, int fitnessCoach, int moraleCoach, int doctorQualification,
                int doctorPlayers, int scout, int homeTop, int awayTop, int homeBottom,
                int awayBottom, List<String> competitions, List<Player> players) {
        this.name = name;
        this.shortName = id;
        this.countryId = countryId;
        this.managerId = managerId;
        this.managerLogin = managerLogin;
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

    public String getManagerLogin() {
        return managerLogin;
    }

    public void setManagerLogin(String managerLogin) {
        this.managerLogin = managerLogin;
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
        return Objects.equal(countryId, team.countryId) &&
                Objects.equal(managerId, team.managerId) &&
                Objects.equal(shortName, team.shortName) &&
                Objects.equal(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shortName, name, countryId, managerId);
    }
}
