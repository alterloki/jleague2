package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author ashevenkov 04.02.17 14:25.
 */
@Repository
public class ParamDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getParam(String name) {
        return jdbcTemplate.queryForObject("select value from jparam where name = ?",
                String.class, name);
    }
}
