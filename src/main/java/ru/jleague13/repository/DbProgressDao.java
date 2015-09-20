package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.ProgressType;

/**
 * @author ashevenkov 19.09.15 19:02.
 */
@Repository
public class DbProgressDao implements ProgressDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void startProgress(ProgressType progressType) {
        jdbcTemplate.update(
                "insert into progress (type, value) values (?, 1) " +
                        "on duplicate key update value = 1", progressType.toString());
    }

    @Override
    public boolean getProgress(ProgressType progressType) {
        return jdbcTemplate.query("select value from progress where type = ?",
                rs -> rs.next() && rs.getInt(1) == 1, progressType.toString());
    }

    @Override
    public void finishProgress(ProgressType progressType) {
        jdbcTemplate.update(
                "insert into progress (type, value) values (?, 0) " +
                        "on duplicate key update value = 0", progressType.toString());

    }
}
