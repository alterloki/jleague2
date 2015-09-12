package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.NewsItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashevenkov 06.09.15 22:17.
 */
@Repository
public class DbNewsRepository implements NewsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<NewsItem> lastNews(int count) {
        return jdbcTemplate.query(
                "select id, title, text from news_item order by id desc limit ?",
                (rs, i) -> itemFromRs(rs),
                count);
    }

    @Override
    public NewsItem getNewsItem(int id) {
        return jdbcTemplate.query(
                "select id, title, text from news_item where id = ?",
                this::itemFromRs,
                id);
    }

    @Override
    public List<NewsItem> lastNewsBefore(int num, int count) {
        return jdbcTemplate.query(
                "select id, title, text from (select * from news_item order by id desc) a where @rownum > ? and @rownum <= ? + ?",
                (rs, i) -> itemFromRs(rs),
                num, num, count);
    }

    private NewsItem itemFromRs(ResultSet rs) throws SQLException {
        return new NewsItem(rs.getInt("id"), rs.getString("text"), rs.getString("text"));
    }
}
