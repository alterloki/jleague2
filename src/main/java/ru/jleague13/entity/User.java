package ru.jleague13.entity;

import com.google.common.base.Objects;

/**
 * @author ashevenkov 24.09.15 20:42.
 */
public class User {

    private int id;
    private String login;
    private String name;
    private String email;
    private int faId;
    private String password;
    private boolean registered;
    private boolean admin;
    private int icq;
    private String town;
    private String country;

    public User(int id, String login, String name, int faId, String password,
                boolean registered, boolean admin, String email) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.faId = faId;
        this.password = password;
        this.registered = registered;
        this.admin = admin;
        this.email = email;
    }

    public User(int id, String login, String userName, int faId) {
        this.id = id;
        this.login = login;
        this.name = userName;
        this.faId = faId;
    }

    public User(int id, String login, int faId) {
        this.id = id;
        this.login = login;
        this.faId = faId;
    }

    public User() {
    }

    public User(int id, String login, String name,
                int faId, String password, boolean registered,
                boolean admin, String email,
                String town, String country, int icq) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.faId = faId;
        this.password = password;
        this.registered = registered;
        this.admin = admin;
        this.email = email;
        this.town = town;
        this.country = country;
        this.icq = icq;
    }

    public int getIcq() {
        return icq;
    }

    public String getTown() {
        return town;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFaId() {
        return faId;
    }

    public void setFaId(int faId) {
        this.faId = faId;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(faId, user.faId) &&
                Objects.equal(login, user.login) &&
                Objects.equal(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login, name, faId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", faId=" + faId +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", admin=" + admin +
                '}';
    }
}
