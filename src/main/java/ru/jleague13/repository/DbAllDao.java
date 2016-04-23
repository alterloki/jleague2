package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author ashevenkov 23.04.16 14:16.
 */
@Repository
public class DbAllDao implements AllDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean haveAllOnDate(Date date) {
        return false;
    }
}
