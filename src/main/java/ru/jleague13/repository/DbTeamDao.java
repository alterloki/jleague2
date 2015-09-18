package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.Team;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ashevenkov 11.09.15 0:57.
 */
@Repository
public class DbTeamDao implements TeamDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Team> getTeams() {
        return jdbcTemplate.query(
                "select id, short_name, name, country_id from team",
                (resultSet, i) -> teamFromRs(resultSet));
    }

    @Override
    public List<Team> getCountryTeams(int countryId) {
        return jdbcTemplate.query(
                "select id, short_name, name, country_id from team where country_id = ?",
                (resultSet, i) -> teamFromRs(resultSet), countryId);
    }

    @Override
    public void deleteTeam(int teamId) {
        jdbcTemplate.update("delete from team where id = ?", teamId);
    }

    @Override
    public int saveTeam(Team team) {
        if(team.getId() > 0) {
            jdbcTemplate.update("update team set short_name = ?, name = ?, country_id = ? where id = ?",
                    team.getShortName(), team.getName(), team.getCountryId(), team.getId());
            return team.getId();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps =
                        connection.prepareStatement("insert into team (short_name, name, country_id) values (?,?,?)", new String[]{"id"});
                ps.setString(1, team.getShortName());
                ps.setString(2, team.getName());
                ps.setInt(3, team.getCountryId());
                return ps;
            }, keyHolder);
            return keyHolder.getKey().intValue();
        }
    }

    @Override
    public void deleteCountryTeams(int id) {
        jdbcTemplate.update("delete from team where country_id = ?", id);
    }

    private Team teamFromRs(ResultSet rs) throws SQLException {
        return new Team(rs.getInt("id"), rs.getString("short_name"),
                rs.getString("name"), rs.getInt("country_id"));
    }
}
