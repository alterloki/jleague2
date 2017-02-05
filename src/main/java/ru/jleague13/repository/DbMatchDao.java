package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Match;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        if(match.getId() > 0) {
            jdbcTemplate.update(
                    "update match set event_date = ?, event_type = ?, owner_team_id = ?, " +
                            "guest_team_id = ?, owner_score = ?, guest_score = ? where id = ?",
                    match.getMatchEvent().getDay(), match.getMatchEvent().getEventType().ordinal(),
                    match.getOwnerTeamId(), match.getGuestTeamId(), match.getOwnerScore(), match.getGuestScore());
            return match.getId();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps =
                        connection.prepareStatement(
                                "insert into jmatch " +
                                        "(event_date, event_type, owner_team_id, guest_team_id, owner_score, guest_score)" +
                                        " values (?,?,?,?,?,?)", new String[]{"id"});
                ps.setTimestamp(1, new Timestamp(match.getMatchEvent().getDay().getTime()));
                ps.setInt(2, match.getMatchEvent().getEventType().ordinal());
                ps.setInt(3, match.getOwnerTeamId());
                ps.setInt(4, match.getGuestTeamId());
                ps.setInt(5, match.getOwnerScore());
                ps.setInt(6, match.getGuestScore());
                return ps;
            }, keyHolder);
            int id = keyHolder.getKey().intValue();
            match.setId(id);
            return id;
        }
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

    @Override
    public void deleteMatch(Match match) {
        if(match.getId() > 0) {
            jdbcTemplate.update("delete from jmatch where id = ?", match.getId());
        }
    }
}
