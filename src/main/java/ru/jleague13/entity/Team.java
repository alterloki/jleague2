package ru.jleague13.entity;

import com.google.common.base.Objects;

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

    public Team() {
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
