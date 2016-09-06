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
import java.util.List;

/**
 * @author ashevenkov 27.09.15 17:57.
 */
@Repository
public class DbUserDao implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static String FIELDS = "id, fa_id, login, name, password, registered, admin, email";

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("select " + FIELDS + " from users",
                (rs, i) -> userFromResultSet(rs));
    }

    @Override
    public List<User> getRegisteredUsers() {
        return jdbcTemplate.query("select " + FIELDS + " from users where registered = 1",
                (rs, i) -> userFromResultSet(rs));
    }

    @Override
    public void deleteUser(int userId) {
        jdbcTemplate.update("delete from users where id = ?", userId);
    }

    @Override
    public User getUserByLogin(String login) {
        return jdbcTemplate.query("select " + FIELDS + " from users where login = ?",
                rs -> {
                    if(rs.next()) {
                        return userFromResultSet(rs);
                    }
                    return null;
                }, login);
    }

    @Override
    public User getUser(int userId) {
        return jdbcTemplate.query("select " + FIELDS + " from users where id = ?",
                rs -> {
                    if(rs.next()) {
                        return userFromResultSet(rs);
                    }
                    return null;
                }, userId);
    }

    private User userFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                rs.getInt("fa_id"), 0, "", "", 0,
                rs.getString("login"), rs.getString("password"),
                rs.getInt("registered") == 1, rs.getInt("admin") == 1);
    }

    @Override
    public int saveUser(User user) {
        if(user.getId() > 0) {
            jdbcTemplate.update("update users set " +
                            "login = ?, name = ?, fa_id = ?, password = ?, " +
                            "admin = ?, registered = ?, email = ? where id = ?",
                    user.getLogin(), user.getName(), user.getFaId(), user.getPassword(),
                    user.isAdmin() ? 1 : 0, user.isRegistered() ? 1 : 0, user.getEmail(),
                    user.getId());
            return user.getId();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps =
                        connection.prepareStatement("insert into users " +
                                "(login, name, fa_id, password, admin, registered, email)" +
                                " values (?,?,?,?,?,?,?)", new String[]{"id"});
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getName());
                ps.setInt(3, user.getFaId());
                ps.setString(4, user.getPassword());
                ps.setInt(5, user.isAdmin() ? 1 : 0);
                ps.setInt(6, user.isRegistered() ? 1 : 0);
                ps.setString(7, user.getEmail());
                return ps;
            }, keyHolder);
            int id = keyHolder.getKey().intValue();
            user.setId(id);
            return id;
        }
    }

    @Override
    public User getUserByFaId(int faId) {
        return jdbcTemplate.query("select " + FIELDS + " from users where fa_id = ?",
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
