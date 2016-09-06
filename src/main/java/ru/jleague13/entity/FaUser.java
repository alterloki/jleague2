package ru.jleague13.entity;

/**
 * @author ashevenkov 25.04.16 15:49.
 */
public class FaUser {

    private int id;
    private String name;
    private String email;
    private int faId;
    private int icq;
    private String town;
    private String country;
    private int managerFinance;

    public FaUser() {
    }

    public FaUser(int id, String name, String email, int faId, int icq, String town, String country, int managerFinance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.faId = faId;
        this.icq = icq;
        this.town = town;
        this.country = country;
        this.managerFinance = managerFinance;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFaId() {
        return faId;
    }

    public void setFaId(int faId) {
        this.faId = faId;
    }

    public int getIcq() {
        return icq;
    }

    public void setIcq(int icq) {
        this.icq = icq;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getManagerFinance() {
        return managerFinance;
    }

    public void setManagerFinance(int managerFinance) {
        this.managerFinance = managerFinance;
    }
}
