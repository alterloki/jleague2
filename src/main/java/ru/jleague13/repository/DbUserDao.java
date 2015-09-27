package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ashevenkov 27.09.15 17:57.
 */
@Repository
public class DbUserDao implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUser(int userId) {
        return jdbcTemplate.query("select id, fa_id, login, name from users where id = ?",
                rs -> {
                    if(rs.next()) {
                        return userFromResultSet(rs);
                    }
                    return null;
                }, userId);
    }

    private User userFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("login"),
                rs.getString("name"), rs.getInt("fa_id"));
    }

    @Override
    public int saveUser(User user) {
        if(user.getId() > 0) {
            jdbcTemplate.update("update users set login = ?, name = ?, fa_id = ? where id = ?",
                    user.getLogin(), user.getName(), user.getFaId(), user.getId());
            return user.getId();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps =
                        connection.prepareStatement("insert into users (login, name, fa_id) values (?,?,?)", new String[]{"id"});
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getName());
                ps.setInt(3, user.getFaId());
                return ps;
            }, keyHolder);
            int id = keyHolder.getKey().intValue();
            user.setId(id);
            return id;
        }
    }

    @Override
    public User getUserByFaId(int faId) {
        return jdbcTemplate.query("select id, fa_id, login, name from users where fa_id = ?",
                rs -> {
                    if(rs.next()) {
                        return userFromResultSet(rs);
                    }
                    return null;
                }, faId);
    }

    @Override
    public boolean haveUserByFaId(int faId) {
        return getUserByFaId(faId) != null;
    }
}
