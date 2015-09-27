package ru.jleague13.entity;

import com.google.common.base.Objects;

/**
 * @author ashevenkov 24.09.15 20:42.
 */
public class User {

    private int id;
    private String login;
    private String name;
    private int faId;

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
}
