package ru.jleague13.repository;

import ru.jleague13.entity.User;

import java.util.List;

/**
 * @author ashevenkov 27.09.15 17:30.
 */
public interface UserDao {

    User getUser(int userId);

    int saveUser(User user);

    User getUserByFaId(int faId);

    boolean haveUserByFaId(int faId);

    List<User> getAllUsers();

    List<User> getRegisteredUsers();

    void deleteUser(int userId);

    User getUserByLogin(String login);
}
