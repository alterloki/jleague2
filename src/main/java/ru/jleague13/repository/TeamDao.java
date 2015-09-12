package ru.jleague13.repository;

import ru.jleague13.entity.Team;

import java.util.List;

/**
 * @author ashevenkov 10.09.15 23:36.
 */
public interface TeamDao {

    List<Team> getTeams();

    void deleteTeam(int id);

    int saveTeam(Team team);

}
