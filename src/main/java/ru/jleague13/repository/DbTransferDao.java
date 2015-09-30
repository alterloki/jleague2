package ru.jleague13.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.Player;
import ru.jleague13.entity.PlayerType;
import ru.jleague13.entity.Transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 26.09.15 23:08.
 */
@Repository
public class DbTransferDao implements TransferDao {

    private Log log = LogFactory.getLog(DbTransferDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveTransfer(Transfer transfer) {
        if(haveTransfer(transfer.getDate())) {
            jdbcTemplate.update("delete from transfer_player where transfer_date = ?", transfer.getDate());
        } else {
            jdbcTemplate.update("insert into transfer (transfer_date) values (?)", transfer.getDate());
        }
        jdbcTemplate.batchUpdate("insert into transfer_player " +
                "(transfer_date, player_id, name, player_type, country, seller, buyer, age, talent," +
                "experience, strength, health, price, salary, payed, abilities, shooting, handling," +
                "reflexes, passing, crossing, dribbling, tackling, heading, speed, stamina, birthtour)" +
                " values " +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                transfer.getPlayers().stream().map(player -> new Object[]{
                        transfer.getDate(), player.getPlayerId(), player.getName(), player.getPlayerType().name(),
                        player.getCountry(), player.getSeller(), player.getBuyer(), player.getAge(), player.getTalent(),
                        player.getExperience(), player.getStrength(), player.getHealth(), player.getPrice(),
                        player.getSalary(), player.getPayed(), player.getAbilities(), player.getShooting(),
                        player.getHandling(), player.getReflexes(), player.getPassing(), player.getCross(),
                        player.getDribbling(), player.getTackling(), player.getHeading(), player.getSpeed(),
                        player.getStamina(), player.getBirthtour()
                }).collect(Collectors.toList()));
    }

    @Override
    public boolean haveTransfer(Date date) {
        return jdbcTemplate.queryForObject(
                "select count(1) from transfer where transfer_date = ?", Integer.class, date) > 0;
    }

    @Override
    public Transfer loadTransfer(Date date) {
        if(haveTransfer(date)) {
            return new Transfer(date, jdbcTemplate.query(
                    "select id, player_id, name, player_type, country, seller, buyer, age, talent," +
                            "experience, strength, health, price, salary, payed, abilities, shooting, handling," +
                            "reflexes, passing, crossing, dribbling, tackling, heading, speed, stamina, birthtour " +
                            "from transfer_player where transfer_date = ?",
                    (rs, i) -> {
                        Player player = new Player();
                        player.setId(rs.getInt("id"));
                        player.setPlayerId(rs.getInt("player_id"));
                        player.setName(rs.getString("name"));
                        player.setPlayerType(PlayerType.valueOf(rs.getString("player_type")));
                        player.setCountry(rs.getString("country"));
                        player.setSeller(rs.getString("seller"));
                        player.setBuyer(rs.getString("buyer"));
                        player.setAge(rs.getInt("age"));
                        player.setTalent(rs.getInt("talent"));
                        player.setExperience(rs.getInt("experience"));
                        player.setStrength(rs.getInt("strength"));
                        player.setHealth(rs.getInt("health"));
                        player.setPrice(rs.getInt("price"));
                        player.setSalary(rs.getInt("salary"));
                        player.setPayed(rs.getInt("payed"));
                        player.setAbilities(rs.getString("abilities"));
                        player.setShooting(rs.getInt("shooting"));
                        player.setHandling(rs.getInt("handling"));
                        player.setReflexes(rs.getInt("reflexes"));
                        player.setPassing(rs.getInt("passing"));
                        player.setCross(rs.getInt("crossing"));
                        player.setDribbling(rs.getInt("dribbling"));
                        player.setTackling(rs.getInt("tackling"));
                        player.setHeading(rs.getInt("heading"));
                        player.setSpeed(rs.getInt("speed"));
                        player.setStamina(rs.getInt("stamina"));
                        player.setBirthtour(rs.getInt("birthtour"));
                        return player;
                    }, date));
        }
        return null;
    }

    @Override
    public List<Date> allTransferDates() {
        return jdbcTemplate.queryForList("select transfer_date from transfer order by transfer_date desc", Date.class);
    }

    @Override
    public Transfer readTransfer(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        return readTransferList(br);
    }

    public Transfer readTransferList(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (!s.equals("format=1")) {
            log.error("Inappropriate file format");
            return null;
        }
        s = reader.readLine();
        SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = dateParser.parse(s);
        } catch (ParseException e) {
            log.error("Inappropriate date format");
            return null;
        }
        List<Player> players = new ArrayList<>();
        Player curr = readTransferPlayer(reader);
        while (curr != null) {
            players.add(curr);
            curr = readTransferPlayer(reader);
        }
        return new Transfer(date, players);
    }

    public Player readTransferPlayer(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        String[] parsedData = s.split("/");
        if (parsedData.length == 1) {
            return null;
        }
        int pos = 0;
        int id = Integer.valueOf(parsedData[pos++]);
        String name = parsedData[pos++];
        String nationality = parsedData[pos++];
        String previousTeam = parsedData[pos++];
        PlayerType position = PlayerType.resolveByFormValue(parsedData[pos++]);
        int age = Integer.valueOf(parsedData[pos++]);
        int talent = Integer.valueOf(parsedData[pos++]);
        int salary = Integer.valueOf(parsedData[pos++]);
        int strength = Integer.valueOf(parsedData[pos++]);
        int health = Integer.valueOf(parsedData[pos++]);
        String abilities = parsedData[pos++];
        int price = Integer.valueOf(parsedData[pos++]);
        Player transferPlayer = new Player();
        transferPlayer.setPlayerId(id);
        transferPlayer.setName(name);
        transferPlayer.setCountry(nationality);
        transferPlayer.setSeller(previousTeam);
        transferPlayer.setAge(age);
        transferPlayer.setPlayerType(position);
        transferPlayer.setTalent(talent);
        transferPlayer.setSalary(salary);
        transferPlayer.setStrength(strength);
        transferPlayer.setHealth(health);
        transferPlayer.setAbilities(abilities);
        transferPlayer.setPrice(price);
        transferPlayer.setShooting(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setHandling(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setReflexes(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setPassing(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setCross(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setDribbling(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setTackling(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setHeading(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setSpeed(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setStamina(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setBirthtour(Integer.valueOf(parsedData[pos]));
        return transferPlayer;
    }
}
