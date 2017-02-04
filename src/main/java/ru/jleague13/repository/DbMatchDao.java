package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Match;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 29.01.17 16:05.
 */
@Repository
public class DbMatchDao implements MatchDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String MATCH_FULL_FIELDS =
            "m.id, m.event_type, m.event_date, " +
            "m.owner_team_id, t1.name owner_team_name, m.owner_score, " +
            "m.guest_team_id, t2.name guest_team_name, m.guest_score";

    private static final String MATCH_TABLES = "jmatch m, team t1, team t2";

    private Match matchFromRs(ResultSet rs, Event event) throws SQLException {
        return new Match(rs.getInt("id"), rs.getInt("owner_team_id"),
                rs.getString("owner_team_name"), rs.getInt("guest_team_id"),
                rs.getString("guest_team_name"), rs.getInt("owner_score"),
                rs.getInt("guest_score"), event);
    }

    @Override
    public List<Match> loadMatches(Event event) {
        return jdbcTemplate.query(
                "select " + MATCH_FULL_FIELDS +
                        " from " + MATCH_TABLES +
                        " where m.owner_team_id = t1.id " +
                        " and m.guest_team_id = t2.id " +
                        " and m.event_date = ? " +
                        " and m.event_type = ?",
                (resultSet, i) -> matchFromRs(resultSet, event),
                event.getDay(), event.getEventType().ordinal());
    }

    @Override
    public int saveMatch(Match match) {
        //todo implement
        return 0;
    }

    @Override
    public Match loadMatchById(int id) {
        return jdbcTemplate.query(
                "select " + MATCH_FULL_FIELDS +
                        " from " + MATCH_TABLES +
                        " where m.owner_team_id = t1.id " +
                        " and m.guest_team_id = t2.id and m.id = ?",
                rs -> {
                    if(rs.next()) {
                        return matchFromRs(rs, new Event(rs.getDate("event_date")));
                    }
                    return null;
                }, id);
    }
}
