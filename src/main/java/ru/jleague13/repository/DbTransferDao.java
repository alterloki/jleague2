package ru.jleague13.repository;

import com.google.common.io.CharStreams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.*;
import ru.jleague13.util.HelperUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
                        player.getSalary(), player.getPayed(), player.getAbilitiesString(), player.getAbilities().getShooting(),
                        player.getAbilities().getHandling(), player.getAbilities().getReflexes(),
                        player.getAbilities().getPassing(), player.getAbilities().getCross(),
                        player.getAbilities().getDribbling(), player.getAbilities().getTackling(),
                        player.getAbilities().getHeading(), player.getAbilities().getSpeed(),
                        player.getAbilities().getStamina(), player.getBirthtour()
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
                        player.setAbilitiesString(rs.getString("abilities"));
                        Abilities abilities = new Abilities();
                        abilities.setShooting(rs.getInt("shooting"));
                        abilities.setHandling(rs.getInt("handling"));
                        abilities.setReflexes(rs.getInt("reflexes"));
                        abilities.setPassing(rs.getInt("passing"));
                        abilities.setCross(rs.getInt("crossing"));
                        abilities.setDribbling(rs.getInt("dribbling"));
                        abilities.setTackling(rs.getInt("tackling"));
                        abilities.setHeading(rs.getInt("heading"));
                        abilities.setSpeed(rs.getInt("speed"));
                        abilities.setStamina(rs.getInt("stamina"));
                        player.setAbilities(abilities);
                        player.setBirthtour(rs.getInt("birthtour"));
                        return player;
                    }, date));
        }
        return new Transfer(date, new ArrayList<>());
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

    @Override
    public void saveTransferResult(Player player) {
        jdbcTemplate.update("update transfer_player set payed = ?, buyer = ? where id = ?",
                player.getPayed(), player.getBuyer(), player.getId());
    }

    @Override
    public Map<String, Player> readTransferResult(Reader reader, Date transferDate) throws IOException {
        Transfer transfer = loadTransfer(transferDate);
        Map<String, Player> allPlayersMap = HelperUtils.toMap(transfer);
        Map<String, Player> map = new HashMap<>();
        String str = CharStreams.toString(reader);
        Document doc = Jsoup.parse(str);
        Elements select = doc.select("table.alternated-rows-bg").select("tbody > tr");
        for(int i = 1; i < select.size(); i++) {
            Element lineElement = select.get(i);
            Player player = extractPlayer(lineElement, allPlayersMap);
            map.put(player.getName(), player);
        }
        return map;
    }

    @Override
    public List<TransferQueryPlayer> readTransferQuery(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        for(int i = 0; i < 5; i++) {
            br.readLine();
        }
        List<String> pure = new ArrayList<>();
        String line = br.readLine();
        while(line != null && !line.startsWith("*")) {
            pure.add(line);
            line = br.readLine();
        }
        List<TransferQueryPlayer> result = new ArrayList<>();
        for(int i = 0; i < pure.size() - 2; i++) {
            String[] parts = pure.get(i).split("/");
            result.add(new TransferQueryPlayer(parts[1],
                    Integer.parseInt(parts[4]), Integer.parseInt(parts[3])));
        }
        return result;
    }

    @Override
    public Player extractPlayer(Element elem, Map<String, Player> playerMap) {
        Elements fields = elem.select("td");
        String name = fields.get(1).text();
        int price = Integer.parseInt(fields.get(11).text());
        String buyer = fields.get(9).select("a").text();
        Player player = new Player();
        player.setName(name);
        player.setPayed(price);
        player.setBuyer(buyer);
        return player;
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
        transferPlayer.setAbilitiesString(abilities);
        transferPlayer.setPrice(price);
        Abilities abs = new Abilities();
        abs.setShooting(Integer.valueOf(parsedData[pos++]));
        abs.setHandling(Integer.valueOf(parsedData[pos++]));
        abs.setReflexes(Integer.valueOf(parsedData[pos++]));
        abs.setPassing(Integer.valueOf(parsedData[pos++]));
        abs.setCross(Integer.valueOf(parsedData[pos++]));
        abs.setDribbling(Integer.valueOf(parsedData[pos++]));
        abs.setTackling(Integer.valueOf(parsedData[pos++]));
        abs.setHeading(Integer.valueOf(parsedData[pos++]));
        abs.setSpeed(Integer.valueOf(parsedData[pos++]));
        abs.setStamina(Integer.valueOf(parsedData[pos++]));
        transferPlayer.setAbilities(abs);
        transferPlayer.setBirthtour(Integer.valueOf(parsedData[pos]));
        return transferPlayer;
    }
}
