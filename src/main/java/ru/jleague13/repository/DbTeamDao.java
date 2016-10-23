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
import ru.jleague13.entity.TeamInfo;
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

    private static String FULL_FIELDS = "t.id, t.short_name, t.name, t.country_id," +
                                        "u.login manager_login, u.id manager_id, t.division";

    @Override
    public List<Team> getTeamsBySubstr() {
        return jdbcTemplate.query(
                "select  " + FULL_FIELDS +
                        " from team t left outer join users u on t.manager_id = u.id",
                (resultSet, i) -> teamFromRs(resultSet));
    }

    @Override
    public List<Team> getCountryTeams(int countryId) {
        return jdbcTemplate.query(
                "select " + FULL_FIELDS +
                        " from team t left outer join users u on t.manager_id = u.id " +
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
                try {
                    downloadImages.downloadEmblem(team);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("Download emblem old for " + team);
            }
            jdbcTemplate.update("update team set short_name = ?, name = ?, country_id = ?, manager_id = ?, division = ? " +
                            " where id = ?",
                    team.getShortName(), team.getName(), team.getCountryId(), team.getManagerId(), team.getDiv(), team.getId());
            return team.getId();
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps =
                        connection.prepareStatement("insert into team (short_name, name, country_id, manager_id, division)" +
                                " values (?,?,?,?,?)", new String[]{"id"});
                ps.setString(1, team.getShortName());
                ps.setString(2, team.getName());
                ps.setInt(3, team.getCountryId());
                ps.setInt(4, team.getManagerId());
                ps.setInt(5, team.getDiv());
                return ps;
            }, keyHolder);
            int id = keyHolder.getKey().intValue();
            team.setId(id);
            log.info("Download emblem new for " + team.getShortName());
            try {
                downloadImages.downloadEmblem(team);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void addView(int teamId) {
        jdbcTemplate.update("update team set views = (select views from team where id = ?) + 1 where id = ?",
                teamId, teamId);
    }

    @Override
    public Team getTeam(int id) {
        return jdbcTemplate.query(
                "select " + FULL_FIELDS +" from team t left outer join users u on t.manager_id = u.id where t.id = ?",
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
                "select " + FULL_FIELDS + " from team t left outer join users u on t.manager_id = u.id where t.country_id = " +
                        "(select id from country where fa_index = ?) and manager_id > 0",
                (resultSet, i) -> teamFromRs(resultSet), "JPN");
    }

    @Override
    public List<Team> getAllTeams() {
        return jdbcTemplate.query(
                "select " + FULL_FIELDS + " from team t left outer join users u on t.manager_id = u.id",
                (resultSet, i) -> teamFromRs(resultSet), "JPN");
    }

    @Override
    public List<Team> getTeamsBySubstr(String substring, int count) {
        return jdbcTemplate.query(
                "select * from (select " + FULL_FIELDS + " from team t left outer join users u on t.manager_id = u.id where " +
                        "lower(t.name) like concat('%',?,'%') order by t.views desc, t.name) v1 where v1.manager_id > 0 limit ?",
                (resultSet, i) -> teamFromRs(resultSet), substring.toLowerCase(), count);
    }

    @Override
    public List<Team> getTopTeams(int number) {
        return jdbcTemplate.query(
                "select * from (select " + FULL_FIELDS + " from team t left outer join users u " +
                        "on t.manager_id = u.id order by t.views desc, t.name) v1 where v1.manager_id > 0 limit ?",
                (resultSet, i) -> teamFromRs(resultSet), number);
    }

    @Override
    public void deleteTeamInfoByAllId(int allId) {
        jdbcTemplate.update("delete from team_info where all_id = ?", allId);
    }

    @Override
    public void saveTeamInfoByAllId(int allId, List<Team> teams) {
        List<Object[]> params = new ArrayList<>();
        for (Team team : teams) {
            TeamInfo teamInfo = team.getTeamInfo();
            params.add(new Object[]{allId, team.getId(), teamInfo.getGames(), teamInfo.getStadiumCapacity(),
                    teamInfo.getBoom(), teamInfo.getTown(), teamInfo.getTeamFinance(), teamInfo.getStadium(),
                    teamInfo.getStadiumState(), teamInfo.isSportschool() ? 1 : 0, teamInfo.getSportschoolState(),
                    teamInfo.getCoach(), teamInfo.getGoalkeepersCoach(), teamInfo.getDefendersCoach(),
                    teamInfo.getMidfieldersCoach(), teamInfo.getForwardsCoach(), teamInfo.getFitnessCoach(),
                    teamInfo.getMoraleCoach(), teamInfo.getDoctorQualification(), teamInfo.getDoctorPlayers(),
                    teamInfo.getScout(), teamInfo.getHomeTop(), teamInfo.getAwayTop(), teamInfo.getHomeBottom(),
                    teamInfo.getAwayBottom(), stringCompetitions(teamInfo.getCompetitions())});
        }
        jdbcTemplate.batchUpdate("insert into team_info (all_id, team_id, games, stadiumCapacity," +
                "boom, town, teamFinance, stadium, stadiumState, sportschool, sportschoolState," +
                "coach, goalkeepersCoach, defendersCoach, midfieldersCoach, forwardsCoach, fitnessCoach," +
                "moraleCoach, doctorQualification, doctorPlayers, scout, homeTop, awayTop, homeBottom," +
                "awayBottom, competitions) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", params);
    }

    private String stringCompetitions(List<String> competitions) {
        StringBuilder sb = new StringBuilder();
        for (String competition : competitions) {
            if(sb.length() > 0) {
                sb.append(",");
            }
            sb.append(competition);
        }
        return sb.toString();
    }

    private Team teamFromRs(ResultSet rs) throws SQLException {
        Team team = new Team(rs.getInt("id"), rs.getString("short_name"),
                rs.getString("name"), rs.getInt("country_id"));
        team.setEmblem(imagesManager.teamEmblemUrl(team));
        team.setManagerId(rs.getInt("manager_id"));
        team.setDiv(rs.getInt("division"));
        team.setManagerLogin(rs.getString("manager_login"));
        return team;
    }
}
