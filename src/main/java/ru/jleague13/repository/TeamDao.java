package ru.jleague13.repository;

import ru.jleague13.entity.Team;

import java.util.List;

/**
 * @author ashevenkov 10.09.15 23:36.
 */
public interface TeamDao {

    List<Team> getTeams();

    List<Team> getCountryTeams(int countryId);

    void deleteTeam(int teamId);

    int saveTeam(Team team);

    void deleteCountryTeams(int id);

    Team getTeam(int id);
}
