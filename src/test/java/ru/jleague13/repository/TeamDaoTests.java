package ru.jleague13.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.entity.Team;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ashevenkov 11.09.15 22:36.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@Transactional
public class TeamDaoTests {

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() {
        new JdbcTemplate(dataSource).update(
                "insert into team (short_name, name, picture) values (?,?,?)",
                "A", "Android", "http://jleague13.ru/1.jpg");
        new JdbcTemplate(dataSource).update(
                "insert into team (short_name, name, picture) values (?,?,?)",
                "B", "Bear", "http://jleague13.ru/2.jpg");
        new JdbcTemplate(dataSource).update(
                "insert into team (short_name, name, picture) values (?,?,?)",
                "C", "Cereal", "http://jleague13.ru/3.jpg");
    }

    @After
    public void tearDown() {
        new JdbcTemplate(dataSource).update("delete from team");
    }

    @Test
    public void testGetTeams() {
        List<Team> teams = teamDao.getTeams();
        assert teams.size() == 3;
        boolean aWas = false;
        for (Team team : teams) {
            if(team.getShortName().equals("A")) {
                assert team.getName().equals("Android");
                assert team.getPicture().equals("http://jleague13.ru/1.jpg");
                aWas = true;
            }
        }
        assert aWas;
    }

    @Test
    public void testGetDelete() {
        List<Team> teams = teamDao.getTeams();
        int bId = 0;
        boolean bWas = false;
        for (Team team : teams) {
            if(team.getShortName().equals("B")) {
                bId = team.getId();
                bWas = true;
            }
        }
        assert bWas;
        teamDao.deleteTeam(bId);
        teams = teamDao.getTeams();
        assert teams.size() == 2;
        assert teams.get(0).getName().equals("Android") || teams.get(0).getName().equals("Cereal");
        assert teams.get(1).getName().equals("Android") || teams.get(1).getName().equals("Cereal");
    }

    @Test
    public void testUpdate() {
        List<Team> teams = teamDao.getTeams();
        assert teams.size() == 3;
        Team team = null;
        for (Team t : teams) {
            if(t.getShortName().equals("C")) {
                team = t;
            }
        }
        assert team != null;
        team.setShortName("E");
        team.setName("Elastic");
        team.setPicture("http://jleague13.ru/5.jpg");
        teamDao.saveTeam(team);
        teams = teamDao.getTeams();
        assert teams.size() == 3;
        team = null;
        for (Team t : teams) {
            if(t.getShortName().equals("E")) {
                team = t;
            }
        }
        assert team != null;
        assert team.getName().equals("Elastic");
        assert team.getPicture().equals("http://jleague13.ru/5.jpg");
    }

    @Test
    public void testSaveNew() {
        Team team = new Team(0, "D", "Dumbldore", "http://jleague13.ru/4.jpg");
        teamDao.saveTeam(team);
        List<Team> teams = teamDao.getTeams();
        assert teams.size() == 4;
        boolean wasD = false;
        for (Team t : teams) {
            if(t.getShortName().equals("D")) {
                wasD = true;
                assert t.getName().equals("Dumbldore");
                assert t.getPicture().equals("http://jleague13.ru/4.jpg");
            }
        }
        assert wasD;
    }
}
