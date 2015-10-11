package ru.jleague13.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.Jleague2Application;
import ru.jleague13.entity.Player;
import ru.jleague13.entity.PlayerType;
import ru.jleague13.entity.Transfer;

import javax.sql.DataSource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author ashevenkov 26.09.15 22:56.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Jleague2Application.class)
@Transactional
public class TransferDaoTests {

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws ParseException {
        new JdbcTemplate(dataSource).update("delete from transfer_player");
        new JdbcTemplate(dataSource).update("delete from transfer");
    }

    @After
    public void tearDown() {
        new JdbcTemplate(dataSource).update("delete from transfer");
        new JdbcTemplate(dataSource).update("delete from transfer_player");
    }

    @Test
    public void testReadTransfer() throws IOException, ParseException {
        Resource resource = new ClassPathResource("02_09_transfer.b13");
        InputStream resourceInputStream = resource.getInputStream();
        Transfer transfer = transferDao.readTransfer(new InputStreamReader(
                resourceInputStream, "cp1251"));
        assert transfer.getDate().getTime() == new SimpleDateFormat("dd.MM.yyyy").parse("29.08.2015").getTime();
        assert transfer.getPlayers().size() == 1041;
        Player testPlayer = null;
        for (Player player : transfer.getPlayers()) {
            if(player.getPlayerId() == 64139) {
                testPlayer = player;
            }
        }
        assert testPlayer != null;
        assert testPlayer.getName().equals("Адам Мёрфи");
        assert testPlayer.getPrice() == 2239;
    }

    @Test
    public void testReadResult() throws IOException {
        Resource resource = new ClassPathResource("02_09_transfer_result");
        InputStream resourceInputStream = resource.getInputStream();
        Map<String, Player> result = transferDao.readTransferResult(new InputStreamReader(
                resourceInputStream, "cp1251"));
        assert result.size() == 691;
        Player player = result.get("Салвадор Агра");
        assert player.getPayed() == 3132;
    }

    @Test
    public void testSaveLoadTransfer() throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyy").parse("01.09.2015");
        Transfer transfer = new Transfer(date, Arrays.asList(
                new Player(0, 12, "Alex", PlayerType.CF, "Russia", "Akita", "Gifu", 25, 90, 100, 70, 100, 2233, 235,
                        4567, "УСФ", 50, 20, 30, 20, 60, 33, 40, 22, 100, 33, 3)));
        transferDao.saveTransfer(transfer);
        Transfer loaded = transferDao.loadTransfer(date);
        assert loaded.getDate().getTime() == date.getTime();
        assert loaded.getPlayers().size() == 1;
        Player player = loaded.getPlayers().get(0);
        assert player.getPlayerId() == 12;
        assert player.getName().equals("Alex");
        assert player.getPlayerType() == PlayerType.CF;
        assert player.getCountry().equals("Russia");
        assert player.getSeller().equals("Akita");
        assert player.getBuyer().equals("Gifu");
        assert player.getAge() == 25;
        assert player.getTalent() == 90;
        assert player.getExperience() == 100;
        assert player.getStrength() == 70;
        assert player.getHealth() == 100;
        assert player.getPrice() == 2233;
        assert player.getSalary() == 235;
        assert player.getPayed() == 4567;
        assert player.getAbilities().equals("УСФ");
        assert player.getShooting() == 50;
        assert player.getHandling() == 20;
        assert player.getReflexes() == 30;
        assert player.getPassing() == 20;
        assert player.getCross() == 60;
        assert player.getDribbling() == 33;
        assert player.getTackling() == 40;
        assert player.getHeading() == 22;
        assert player.getSpeed() == 100;
        assert player.getStamina() == 33;
        assert player.getBirthtour() == 3;
    }
}
