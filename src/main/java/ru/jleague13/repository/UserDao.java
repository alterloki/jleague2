package ru.jleague13.repository;

import ru.jleague13.entity.User;

/**
 * @author ashevenkov 27.09.15 17:30.
 */
public interface UserDao {

    User getUser(int userId);

    int saveUser(User user);

    User getUserByFaId(int faId);

    boolean haveUserByFaId(int faId);
}
