package ru.jleague13.repository;

import ru.jleague13.entity.Team;

import java.util.List;

/**
 * @author ashevenkov 10.09.15 23:36.
 */
public interface TeamDao {

    List<Team> getTeamsBySubstr();

    List<Team> getCountryTeams(int countryId);

    void deleteTeam(int teamId);

    int saveTeam(Team team);

    void deleteCountryTeams(int id);

    void addView(int teamId);

    Team getTeam(int id);

    List<Team> getJapanLiveTeams();

    List<Team> getAllTeams();

    List<Team> getTeamsBySubstr(String substring, int count);

    List<Team> getTopTeams(int number);

    void deleteTeamInfoByAllId(int allId);

    void saveTeamInfoByAllId(int allId, List<Team> teams);
}
