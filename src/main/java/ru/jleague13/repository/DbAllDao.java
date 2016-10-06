package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.jleague13.all.AllInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ashevenkov 23.04.16 14:16.
 */
@Repository
public class DbAllDao implements AllDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean haveAllOnDate(Date date) {
        return jdbcTemplate.queryForObject(
                "select count(1) from all_zip where all_date = ?",
                Integer.class, date) > 0;
    }

    @Override
    public int registerAll(Date date) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement("insert into all_zip (all_date, parsed) values (?, 0)",
                            new String[]{"id"});
            ps.setDate(1, new java.sql.Date(date.getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public AllInfo getAllInfoOnDate(Date date) {
        return jdbcTemplate.query(
                "select id, all_date, parsed from all_zip where all_date = ?",
                rs -> {
                    if (rs.next()) {
                        return fromResultSet(rs);
                    } else {
                        return null;
                    }
                }, date);
    }

    @Override
    public AllInfo getById(int allId) {
        return jdbcTemplate.query(
                "select id, all_date, parsed from all_zip where id = ?",
                rs -> {
                    if (rs.next()) {
                        return fromResultSet(rs);
                    } else {
                        return null;
                    }
                }, allId);
    }

    @Override
    public AllInfo getLastAll() {
        return jdbcTemplate.query(
                "select id, all_date, parsed from all_zip where all_date = (select max(all_date) from all_zip)",
                rs -> {
                    if (rs.next()) {
                        return fromResultSet(rs);
                    } else {
                        return null;
                    }
                });
    }

    private AllInfo fromResultSet(ResultSet resultSet) throws SQLException {
        return new AllInfo(
                resultSet.getInt("id"),
                resultSet.getDate("all_date"),
                resultSet.getInt("parsed") > 0);
    }

    @Override
    public void makeParsed(Date date) {
        jdbcTemplate.update("update all_zip set parsed = 1 where all_date = ?", date);
    }

    @Override
    public List<AllInfo> getRegistered() {
        return jdbcTemplate.query("select id, all_date, parsed from all_zip order by all_date",
                (rs, i) -> fromResultSet(rs));
    }

    @Override
    public void deleteAllBefore(Date date) {
        jdbcTemplate.update("delete from all_zip where all_date < ?", date);
    }
}
