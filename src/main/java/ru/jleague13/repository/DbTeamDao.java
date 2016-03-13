package ru.jleague13.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.jleague13.download.DownloadImages;
import ru.jleague13.entity.Team;
import ru.jleague13.images.ImagesManager;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashevenkov 11.09.15 0:57.
 */
@Repository
public class DbTeamDao implements TeamDao {

    private Log log = LogFactory.getLog(DbTeamDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ImagesManager imagesManager;
    @Autowired
    private DownloadImages downloadImages;

    @Override
    public List<Team> getTeams() {
        return jdbcTemplate.query(
                "select t.id, t.short_name, t.name, t.country_id," +
                        "u.login manager_login, u.id manager_id " +
                        "from team t left outer join users u on t.manager_id = u.id",
                (resultSet, i) -> teamFromRs(resultSet));
    }

    @Override
    public List<Team> getCountryTeams(int countryId) {
        return jdbcTemplate.query(
                "select t.id, t.short_name, t.name, t.country_id, " +
                        "u.login manager_login, u.id manager_id " +
                        "from team t left outer join users u on t.manager_id = u.id " +
                        "where t.country_id = ?",
                (resultSet, i) -> teamFromRs(resultSet), countryId);
    }

    @Override
    public void deleteTeam(int teamId) {
        jdbcTemplate.update("delete from team where id = ?", teamId);
    }

    @Override
    public int saveTeam(Team team) {
        if(team.getId() > 0) {
            Team oldTeam = getTeam(team.getId());
            if(team.getShortName() != null && !team.getShortName().equals(oldTeam.getShortName())) {
                /*try {
                    downloadImages.downloadEmblem(team);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                log.info("Download emblem old for " + team);
            }
            jdbcTemplate.update("update team set short_name = ?, name = ?, country_id = ?, manager_id = ? where id = ?",
                    team.getShortName(), team.getName(), team.getCountryId(), team.getManagerId(), team.getId());
            return team.getId();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps =
                        connection.prepareStatement("insert into team (short_name, name, country_id, manager_id)" +
                                " values (?,?,?,?)", new String[]{"id"});
                ps.setString(1, team.getShortName());
                ps.setString(2, team.getName());
                ps.setInt(3, team.getCountryId());
                ps.setInt(4, team.getManagerId());
                return ps;
            }, keyHolder);
            int id = keyHolder.getKey().intValue();
            team.setId(id);
            log.info("Download emblem new for " + team.getShortName());
            /*try {
                downloadImages.downloadEmblem(team);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return id;
        }
    }

    @Override
    public void deleteCountryTeams(int countryId) {
        List<Team> countryTeams = getCountryTeams(countryId);
        for (Team countryTeam : countryTeams) {
            imagesManager.deleteTeamEmblem(countryTeam);
        }
        jdbcTemplate.update("delete from team where country_id = ?", countryId);
    }

    @Override
    public Team getTeam(int id) {
        return jdbcTemplate.query(
                "select t.id, t.short_name, t.name, t.country_id," +
                        "u.login manager_login, u.id manager_id " +
                        "from team t left outer join users u on t.manager_id = u.id where t.id = ?",
                rs -> {
                    if(rs.next()) {
                        return teamFromRs(rs);
                    }
                    return null;
                }, id);
    }

    @Override
    public List<Team> getJapanLiveTeams() {
        return jdbcTemplate.query(
                "select t.id, t.short_name, t.name, t.country_id," +
                        "u.login manager_login, u.id manager_id from team t left outer join users u on t.manager_id = u.id where t.country_id = " +
                        "(select id from country where fa_index = ?) and manager_id > 0",
                (resultSet, i) -> teamFromRs(resultSet), "JPN");
    }

    private Team teamFromRs(ResultSet rs) throws SQLException {
        Team team = new Team(rs.getInt("id"), rs.getString("short_name"),
                rs.getString("name"), rs.getInt("country_id"));
        team.setEmblem(imagesManager.teamEmblemUrl(team));
        team.setManagerLogin(rs.getString("manager_login"));
        team.setManagerId(rs.getInt("manager_id"));
        return team;
    }
}
