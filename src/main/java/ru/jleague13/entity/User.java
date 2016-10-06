package ru.jleague13.entity;

import com.google.common.base.Objects;

/**
 * @author ashevenkov 24.09.15 20:42.
 */
public class User extends FaUser {

    private String login;
    private String password;
    private boolean registered;
    private boolean admin;

    public User() {
    }

    public User(FaUser faUser, String login, String password, boolean registered, boolean admin) {
        super(faUser.getId(), faUser.getName(), faUser.getEmail(), faUser.getFaId(),
                faUser.getIcq(), faUser.getTown(), faUser.getCountry(), faUser.getManagerFinance());
        this.login = login;
        this.password = password;
        this.registered = registered;
        this.admin = admin;
    }

    public User(int id, String name, String email, int faId, int icq, String town, String country,
                int managerFinance, String login, String password, boolean registered, boolean admin) {
        super(id, name, email, faId, icq, town, country, managerFinance);
        this.login = login;
        this.password = password;
        this.registered = registered;
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

}
