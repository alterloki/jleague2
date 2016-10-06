package ru.jleague13.all;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.jleague13.entity.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author ashevenkov 23.04.16 16:33.
 */
public class AllParser {

    private static final Log log = LogFactory.getLog(AllParser.class);
    private final Map<String, Country> countryMap;
    private final Map<Integer, User> usersMap;

    public AllParser(Map<String, Country> countryMap, Map<Integer, User> usersMap) {
        this.countryMap = countryMap;
        this.usersMap = usersMap;
    }

    public AllZip readAll(InputStream is) throws IOException {
        ZipInputStream zipStream = new ZipInputStream(is);
        zipStream.getNextEntry();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(zipStream, "Cp1251"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error while encoding all file");
        }
        AllZip all = readAllUnzipped(reader);
        reader.close();
        zipStream.close();
        return all;
    }

    private AllZip readAllUnzipped(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (s.compareTo("format=4") != 0) {
            System.err.println("Inappropriate file format");
            return null;
        }
        s = reader.readLine();
        SimpleDateFormat dateParser = new SimpleDateFormat("/dd.MM.yyyy/");
        Date date;
        try {
            date = dateParser.parse(s);
        } catch (ParseException e) {
            log.error("Inappropriate date format");
            return null;
        }

        Map<String, String> competitions = new TreeMap<>();
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

        Map<Integer, FaUser> users = new HashMap<>();
        Map<String, Team> teams = new HashMap<>();
        Team current = readTeam(reader, users);

        while (current != null) {
            teams.put(current.getShortName(), current);
            current = readTeam(reader, users);
        }
        return new AllZip(date, competitions, bankRate, teams, users);
    }

    private Team readTeam(BufferedReader reader, Map<Integer, FaUser> users) throws IOException {
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
        cnt = 1;
        int[] cntArr = new int[1];
        cntArr[0] = cnt;
        FaUser user = parseUserData(parsedData, cntArr, 0);
        users.put(user.getFaId(), user);
        int games = Integer.valueOf(parsedData[cntArr[0]++]);

        s = reader.readLine();
        parsedData = s.split("/");
        cnt = 1;

        int stadiumCapacity = Integer.valueOf(parsedData[cnt++]);
        int stadiumState = Integer.valueOf(parsedData[cnt++]);
        int boom = Integer.valueOf(parsedData[cnt++]);
        int teamFinance = Integer.valueOf(parsedData[cnt++]);
        int managerFinance = Integer.valueOf(parsedData[cnt++]);
        user.setManagerFinance(managerFinance);
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

        TeamInfo teamInfo = new TeamInfo(0, town, games, stadiumCapacity, boom, teamFinance, stadium, stadiumState,
                rating, sportbase, sportbaseState, sportschool, sportschoolState, coach, goalkeepersCoach,
                defendersCoach, midfieldersCoach, forwardsCoach, fitnessCoach, moraleCoach,
                doctorQualification, doctorPlayers, scout, homeTop, awayTop, homeBottom, awayBottom,
                competitions);
        Country countryObj = countryMap.get(country);
        int countryId = 0;
        if(countryObj != null) {
            countryId = countryObj.getId();
        }
        Team team = new Team(0, id, name, countryId, 0,
                calculateDiv(competitions), teamInfo);

        List<Player> players = new ArrayList<>(15);
        Player current = readPlayer(reader);
        while (current != null) {
            current.setClubName(name);
            players.add(current);
            current = readPlayer(reader);
        }
        for (Player player : players) {
            player.setCountry(nationalTeam);
        }
        team.setPlayers(players);
        return team;

    }

    private int calculateDiv(List<String> competitions) {
        for (String competition : competitions) {
            if(competition.contains("3")) {
                return 3;
            } else if(competition.contains("2")) {
                return 2;
            }
        }
        return 1;
    }

    private FaUser parseUserData(String[] parsedData, int[] cnt, int managerFinance) {
        int managerId = Integer.valueOf(parsedData[cnt[0]++]);
        String managerName = parsedData[cnt[0]++];
        String managerTown = parsedData[cnt[0]++];
        String managerCountry = parsedData[cnt[0]++];
        String managerEmail = parsedData[cnt[0]++];
        String icq = null;
        String s = "";
        try {
            s = parsedData[cnt[0]++];
            icq = s.substring(4);
        } catch (Exception e) {
            System.out.println(s);
            System.out.println(Arrays.toString(parsedData));
            e.printStackTrace();
        }
        int uin = 0;
        if (!icq.isEmpty()) {
            try {
                uin = Integer.valueOf(icq);
            } catch (NumberFormatException nfe) {
                uin = 0;
            }
        }
        int id = 0;
        FaUser oldUser = usersMap.get(managerId);
        if(oldUser != null) {
            id = oldUser.getId();
        }
        return new FaUser(id, managerName, managerEmail, managerId, uin, managerTown, managerCountry, managerFinance);
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
                price, salary, 0, "", birthtour,
                new Abilities(shooting, handling, reflexes, passing, crossing, dribbling,
                        tackling, heading, speed, stamina),
                new PlayerGameInfo(number, fitness, morale, disqualification, rest, teamwork, games, goalsTotal,
                        goalsMissed, goalsChamp, mark, gamesCareer, goalsCareer, yellowCards, redCards, transfer,
                        lease, birthplace, birthdate, assists, profit));
    }
}
