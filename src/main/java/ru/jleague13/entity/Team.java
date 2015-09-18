package ru.jleague13.entity;

/**
 * @author ashevenkov 10.09.15 23:36.
 */
public class Team {

    private int id;
    private String shortName;
    private String name;
    private int countryId;

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
}
