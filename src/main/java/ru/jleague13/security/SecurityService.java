package ru.jleague13.security;

import ru.jleague13.entity.FaUser;
import ru.jleague13.entity.User;

/**
 * @author ashevenkov 04.02.17 12:36.
 */
public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);

}