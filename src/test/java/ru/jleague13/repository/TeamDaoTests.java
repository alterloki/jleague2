package ru.jleague13.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.Jleague2Application;
import ru.jleague13.entity.Team;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ashevenkov 11.09.15 22:36.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Jleague2Application.class)
@Transactional
public class TeamDaoTests {

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() {
        new JdbcTemplate(dataSource).update("delete from team");
        new JdbcTemplate(dataSource).update(
                "insert into team (short_name, name, country_id) values (?,?,?)",
                "A", "Android", 1);
        new JdbcTemplate(dataSource).update(
                "insert into team (short_name, name, country_id) values (?,?,?)",
                "B", "Bear", 1);
        new JdbcTemplate(dataSource).update(
                "insert into team (short_name, name, country_id) values (?,?,?)",
                "C", "Cereal", 1);
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
                assert team.getCountryId() == 1;
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
        team.setCountryId(3);
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
        assert team.getCountryId() == 3;
    }

    @Test
    public void testSaveNew() {
        Team team = new Team(0, "D", "Dumbldore", 2);
        teamDao.saveTeam(team);
        List<Team> teams = teamDao.getTeams();
        assert teams.size() == 4;
        boolean wasD = false;
        for (Team t : teams) {
            if(t.getShortName().equals("D")) {
                wasD = true;
                assert t.getName().equals("Dumbldore");
                assert t.getCountryId() == 2;
            }
        }
        assert wasD;
    }
}
