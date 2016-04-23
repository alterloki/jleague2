package ru.jleague13.all;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.reader.ReaderException;
import ru.jleague13.entity.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ashevenkov 23.04.16 16:33.
 */
public class AllParser {

    private Log log = LogFactory.getLog(AllParser.class);

    public AllZip readAll(BufferedReader reader, Map<String, Country> countries,
                          Map<Integer, User> oldUserMap) throws IOException {
        Map<Integer, User> newUserMap = new HashMap<>();

        String s = reader.readLine();
        if (s.compareTo("format=4") != 0) {
            System.err.println("Inappropriate file format");
            return null;
        }
        s = reader.readLine();
        SimpleDateFormat dateParser = new SimpleDateFormat("/dd.MM.yyyy/");
        Date date = null;
        try {
            date = dateParser.parse(s);
        } catch (ParseException e) {
            log.error("Inappropriate date format");
            return null;
        }

        Map<String, String> competitions = new TreeMap<String, String>();
        s = reader.readLine();

        while (!s.equals("/888/")) {
            String[] curr = s.replace('/', ' ').trim().split("=");
            String competitionFull = curr[0];
            String competitionShort = curr[1];
            competitions.put(competitionShort, competitionFull);
            s = reader.readLine();
        }

        s = reader.readLine().replace('/', ' ').trim();
        int bankRate = Integer.valueOf(s);

        Map<String, Team> teams = new HashMap<>();
        Team current = readTeam(reader, countries, oldUserMap, newUserMap);

        while (current != null) {
            teams.put(current.getShortName(), current);
            current = readTeam(reader, countries, oldUserMap, oldUserMap);
        }
        return new AllZip(date, competitions, bankRate, teams);

    }

    private Team readTeam(BufferedReader reader, Map<String, Country> countries,
                          Map<Integer, User> oldUserMap, Map<Integer, User> newUserMap) throws IOException {
        String s = reader.readLine();
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        String[] parsedData = s.split("/");
        int cnt = 1;
        String name = parsedData[cnt++];
        String id = parsedData[cnt++];
        String town = parsedData[cnt++];
        String country = parsedData[cnt++];
        String stadium = parsedData[cnt++];

        String nationalTeam = name;
        if (name.endsWith("-Ю")) {
            nationalTeam = name.substring(0, name.length() - 2);
        }

        s = reader.readLine();
        parsedData = s.split("/");
        int[] cntArr = new int[0];
        cntArr[0] = 1;
        User user = parseUserData(parsedData, cntArr, oldUserMap, newUserMap);
        cnt = cntArr[0];
        int games = Integer.valueOf(parsedData[cnt++]);

        s = reader.readLine();
        parsedData = s.split("/");
        cnt = 1;

        int stadiumCapacity = Integer.valueOf(parsedData[cnt++]);
        int stadiumState = Integer.valueOf(parsedData[cnt++]);
        int boom = Integer.valueOf(parsedData[cnt++]);
        int teamFinance = Integer.valueOf(parsedData[cnt++]);
        int managerFinance = Integer.valueOf(parsedData[cnt++]);
        int rating = Integer.valueOf(parsedData[cnt++]);
        int sportbase = Integer.valueOf(parsedData[cnt++]);
        int sportbaseState = Integer.valueOf(parsedData[cnt++]);

        s = reader.readLine();
        parsedData = s.split("/");
        cnt = 1;

        boolean sportschool = (Integer.valueOf(parsedData[cnt++]) != 0);
        int sportschoolState = Integer.valueOf(parsedData[cnt++]);
        int coach = Integer.valueOf(parsedData[cnt++]) - 200;
        int goalkeepersCoach = Integer.valueOf(parsedData[cnt++]);
        int defendersCoach = Integer.valueOf(parsedData[cnt++]);
        int midfieldersCoach = Integer.valueOf(parsedData[cnt++]);
        int forwardsCoach = Integer.valueOf(parsedData[cnt++]);
        int fitnessCoach = Integer.valueOf(parsedData[cnt++]);
        int moraleCoach = Integer.valueOf(parsedData[cnt++]);
        int doctorQualification = Integer.valueOf(parsedData[cnt++]);
        int doctorPlayers = Integer.valueOf(parsedData[cnt++]);
        int scout = Integer.valueOf(parsedData[cnt++]);

        s = reader.readLine();
        parsedData = s.split("/");
        cnt = 1;
        //hot fix to teams with no colors
        int homeTop = 1;
        if (parsedData.length > 1 && parsedData[cnt] != null && !parsedData[cnt].isEmpty()) {
            homeTop = Integer.valueOf(parsedData[cnt]);
        }
        cnt++;
        int awayTop = 1;
        if (parsedData.length > 2 && parsedData[cnt] != null && !parsedData[cnt].isEmpty()) {
            awayTop = Integer.valueOf(parsedData[cnt]);
        }
        cnt++;
        int homeBottom = 1;
        if (parsedData.length > 3 && parsedData[cnt] != null && !parsedData[cnt].isEmpty()) {
            homeBottom = Integer.valueOf(parsedData[cnt]);
        }
        cnt++;
        int awayBottom = 1;
        if (parsedData.length > 4 && parsedData[cnt] != null && !parsedData[cnt].isEmpty()) {
            awayBottom = Integer.valueOf(parsedData[cnt]);
        }
        s = reader.readLine().replace('/', ' ').trim();
        parsedData = s.split(",");

        List<String> competitions = new ArrayList<>();
        Collections.addAll(competitions, parsedData);

        List<Player> players = new ArrayList<>(15);
        Player current = readPlayer(reader);
        while (current != null) {
            current.setClubName(name);
            players.add(current);
            current = readPlayer(reader);
        }
        if (nationalTeam != null) {
            for (Iterator<Player> iterator = players.iterator(); iterator.hasNext(); ) {
                Player player = iterator.next();
                player.setCountry(nationalTeam);
            }
        }
        updateUsers(newUserMap);
        return new Team(name, id, town, countries.get(country).getId(), stadium, user.getId(),
                user.getLogin(), games, stadiumCapacity, stadiumState, boom, teamFinance, managerFinance,
                rating, sportbase, sportbaseState, sportschool, sportschoolState, coach, goalkeepersCoach, defendersCoach, midfieldersCoach, forwardsCoach, fitnessCoach, moraleCoach,
                doctorQualification, doctorPlayers, scout, homeTop, awayTop, homeBottom, awayBottom, competitions, players);

    }

    private void updateUsers(Map<Integer, User> newUserMap) {
        //todo implement

    }

    private User parseUserData(String[] parsedData, int[] cnt,
                               Map<Integer, User> oldUserMap, Map<Integer, User> newUserMap) {
        int managerId = Integer.valueOf(parsedData[cnt[0]++]);
        String managerName = parsedData[cnt[0]++];
        String managerTown = parsedData[cnt[0]++];
        String managerCountry = parsedData[cnt[0]++];
        String managerEmail = parsedData[cnt[0]++];
        String icq = parsedData[cnt[0]++].substring(4);
        int uin = 0;
        if (!icq.isEmpty()) {
            try {
                uin = Integer.valueOf(icq);
            } catch (NumberFormatException nfe) {
                uin = 0;
            }
        }
        User oldUser = oldUserMap.get(managerId);
        int id = 0;
        User newUser = new User(id, "", managerName, managerId, "", false, false,
                managerEmail, managerTown, managerCountry, uin);
        if (oldUser != null) {
            newUser.setId(oldUser.getId());
            newUser.setLogin(oldUser.getLogin());
            newUser.setPassword(oldUser.getPassword());
            newUser.setRegistered(oldUser.isRegistered());
            newUser.setAdmin(oldUser.isAdmin());
        }
        newUserMap.put(managerId, newUser);

        return newUser;
    }

    public static Player readPlayer(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (s.equals("/999/")) {
            return null;
        }
        String[] parsedData = s.split("/");
        int cnt = 1;
        int number = Integer.valueOf(parsedData[cnt++]);
        String name = parsedData[cnt++];
        String nationality = parsedData[cnt++];
        String nationalityCode = nationality;
        PlayerType position = PlayerType.resolveByFormValue(parsedData[cnt++]);
        int age = Integer.valueOf(parsedData[cnt++]);
        int talent = Integer.valueOf(parsedData[cnt++]);
        int experience = Integer.valueOf(parsedData[cnt++]);
        int fitness = Integer.valueOf(parsedData[cnt++]);
        int morale = Integer.valueOf(parsedData[cnt++]);
        int strength = Integer.valueOf(parsedData[cnt++]);
        int health = Integer.valueOf(parsedData[cnt++]);
        int price = Integer.valueOf(parsedData[cnt++]);
        int salary = Integer.valueOf(parsedData[cnt++]);
        int shooting = Integer.valueOf(parsedData[cnt++]);
        int passing = Integer.valueOf(parsedData[cnt++]);
        int crossing = Integer.valueOf(parsedData[cnt++]);
        int dribbling = Integer.valueOf(parsedData[cnt++]);
        int tackling = Integer.valueOf(parsedData[cnt++]);
        int heading = Integer.valueOf(parsedData[cnt++]);
        int speed = Integer.valueOf(parsedData[cnt++]);
        int stamina = Integer.valueOf(parsedData[cnt++]);
        int reflexes = Integer.valueOf(parsedData[cnt++]);
        int handling = Integer.valueOf(parsedData[cnt++]);
        int disqualification = Integer.valueOf(parsedData[cnt++]);
        int rest = Integer.valueOf(parsedData[cnt++]);
        int teamwork = Integer.valueOf(parsedData[cnt++]);
        int games = Integer.valueOf(parsedData[cnt++]);
        int goalsTotal = Integer.valueOf(parsedData[cnt++]);
        int goalsMissed = Integer.valueOf(parsedData[cnt++]);
        int goalsChamp = Integer.valueOf(parsedData[cnt++]);
        double mark = Double.valueOf(parsedData[cnt++]) / 100.0;
        int gamesCareer = Integer.valueOf(parsedData[cnt++]);
        int goalsCareer = Integer.valueOf(parsedData[cnt++]);
        int yellowCards = Integer.valueOf(parsedData[cnt++]);
        int redCards = Integer.valueOf(parsedData[cnt++]);
        boolean transfer = (Integer.valueOf(parsedData[cnt++]) != 0);
        boolean lease = (Integer.valueOf(parsedData[cnt++]) != 0);
        String birthplace = parsedData[cnt++];
        String date = parsedData[cnt++];
        Date birthdate = null;
        int dateBegin = date.indexOf('(');
        int dateEnd = date.indexOf(')');
        String dateOnly = date.substring(0, dateBegin == -1 ? date.length() : dateBegin);
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthdate = dateParser.parse(dateOnly);
        } catch (ParseException e) {
        }
        int birthtour = 0;
        if (dateBegin != -1) {
            date = date.substring(dateBegin + 1, dateEnd);
            birthtour = Integer.valueOf(date);
        }
        int assists = Integer.valueOf(parsedData[cnt++]);
        int profit = Integer.valueOf(parsedData[cnt++]);
        int id = Integer.valueOf(parsedData[cnt++]);

        return new Player(0, id, name, position, nationality, "", "", age, talent, experience, strength, health,
                price, salary, 0, "", shooting, handling, reflexes, passing, crossing, dribbling, tackling,
                heading, speed, stamina, birthtour);
    }
}
