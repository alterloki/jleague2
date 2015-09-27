package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.NewsArticle;
import ru.jleague13.entity.NewsSnippet;
import ru.jleague13.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ashevenkov 06.09.15 22:17.
 */
@Repository
public class DbNewsDao implements NewsDao {

    public static final int MAX_SNIPPET_LENGTH = 200;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<NewsSnippet> lastNewsSnippets(int count) {
        return jdbcTemplate.query(
                "select ni.id, ni.title, ni.text, ni.post_date, " +
                        "u.id user_id, u.login, u.name user_name, u.fa_id user_fa_id " +
                        "from " +
                        "(" +
                        "  select * from (select * from news_item order by post_date desc) a limit ?" +
                        ") ni, users u " +
                        "where ni.user_id = u.id " +
                        "order by post_date desc",
                (rs, i) -> itemFromRs(rs),
                count);
    }

    @Override
    public NewsArticle getNewsArticle(int id) {
        return jdbcTemplate.query(
                "select ni.id, ni.title, ni.text, ni.post_date, " +
                        "u.id user_id, u.login, u.name user_name, u.fa_id user_fa_id " +
                        "from news_item ni, users u " +
                        "where ni.user_id = u.id " +
                        "and ni.id = ?",
                rs -> {
                    if(rs.next()) {
                        return articleFromRs(rs);
                    } else {
                        return null;
                    }
                },
                id);
    }

    @Override
    public List<NewsSnippet> lastNewsBefore(int num, int count) {
        return jdbcTemplate.query(
                "select ni.id, ni.title, ni.text, ni.post_date, " +
                        "  u.id user_id, u.login, u.name user_name, u.fa_id user_fa_id " +
                        "from " +
                        "  ( " +
                        "select * from ( " +
                        "  SELECT * " +
                        "  FROM (     " +
                        "    SELECT * " +
                        "    FROM " +
                        "      ( " +
                        "        SELECT * " +
                        "        FROM news_item " +
                        "        ORDER BY post_date DESC " +
                        "      ) a " +
                        "    LIMIT ?     " +
                        "  ) b " +
                        "  ORDER BY post_date " +
                        ") c limit ? " +
                        "  ) ni, users u " +
                        "where ni.user_id = u.id " +
                        "order by post_date desc;",
                (rs, i) -> itemFromRs(rs),
                num + count, count);
    }

    private NewsSnippet itemFromRs(ResultSet rs) throws SQLException {
        String text = rs.getString("text");
        if(text.length() > MAX_SNIPPET_LENGTH) {
            text = text.substring(0, 200);
            text += "...";
        }
        return new NewsSnippet(rs.getInt("id"), rs.getString("title"),
                text, rs.getTimestamp("post_date"), userFromRs(rs));
    }

    private NewsArticle articleFromRs(ResultSet rs) throws SQLException {
        return new NewsArticle(
                rs.getInt("id"), rs.getString("title"),
                rs.getString("text"), rs.getTimestamp("post_date"), userFromRs(rs));
    }

    private User userFromRs(ResultSet rs) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getInt("user_fa_id"));
    }

}
