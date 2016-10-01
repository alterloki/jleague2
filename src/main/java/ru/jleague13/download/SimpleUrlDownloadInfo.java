package ru.jleague13.download;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jleague13.all.AllManager;
import ru.jleague13.all.AllParser;
import ru.jleague13.all.AllZip;
import ru.jleague13.entity.*;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.TeamDao;
import ru.jleague13.repository.UserDao;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author ashevenkov 19.09.15 0:09.
 */
@Component
public class SimpleUrlDownloadInfo implements DownloadInfo {

    private Log log = LogFactory.getLog(SimpleUrlDownloadInfo.class);

    @Autowired
    private FaUrlResolver faUrlResolver;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private TeamDao teamDao;
    @Value("${download.dir}")
    private String downloadDir;
    @Autowired
    private AllManager allManager;


    @Override
    public List<Country> downloadCountries() throws IOException {
        ArrayList<Country> countries = new ArrayList<>();
        Document doc = Jsoup.connect(faUrlResolver.getFa13Countries()).get();
        Elements select = doc.select("div[id=team]").select("tr[class^=table]");
        for (int i = 0; i < select.size(); i++) {
            String faId = select.get(i).select("a").attr("href").substring(18);
            String name = select.get(i).select("a").text();
            String faIndex = select.get(i).select("img").attr("src").substring(20, 23);
            countries.add(new Country(0, faId, name, faIndex));
        }
        return countries;
    }

    @Override
    public List<Team> downloadTeams(Country country) throws IOException {
        ArrayList<Team> teams = new ArrayList<>();
        Document doc = Jsoup.connect(faUrlResolver.getFa13Teams(country)).get();
        Elements select = doc.select("div[id=team]").select("tr[class^=table]");
        for (int i = 0; i < select.size(); i++) {
            String shortName = select.get(i).select("a[href^=team]").attr("href").substring(15);
            String name = select.get(i).select("a[href^=team]").select("b").text();
            //TODO fix
            Team team = new Team(0, shortName, name, country.getId());
            String managerLogin = select.get(i).select("a[href^=profile]").text();
            String managerIdString = select.get(i).select("a[href^=profile]").attr("href").substring(16);
            if (managerIdString.length() > 0) {
                int managerId = Integer.parseInt(managerIdString);
                User user = userDao.getUserByFaId(managerId);
                team.setManagerId(managerId);
                if (user != null) {
                    team.setManagerId(user.getId());
                } else {
                    //TODO fix
                    int userId = userDao.saveUser(new User(0, managerLogin, "", managerId, 0, "", "",
                            0, "", "", false, false));
                    team.setManagerId(userId);
                }
            }
            teams.add(team);
        }
        return teams;
    }

    @Override
    public AllZip downloadAll() throws IOException {
        InputStream is = new URL(faUrlResolver.getAllZip()).openStream();
        AllZip all = new AllParser(loadCountryMap()).readAll(is);
        is.close();
        return all;
    }

    @Override
    public void downloadCurrentAllFile() throws IOException {
        URL url = new URL(faUrlResolver.getAllZip());
        byte[] allBytes = ByteStreams.toByteArray(url.openStream());
        Date date = new Date();
        File to = new File(allManager.getAllFileName(date));
        log.info("File = " + to.getAbsolutePath());
        Files.createParentDirs(to);
        Files.write(allBytes, to);
        allManager.registerAllFile(date);
    }

    private Map<String, Team> loadTeams() {
        List<Team> allTeams = teamDao.getAllTeams();
        Map<String, Team> result = new HashMap<>();
        for (Team team : allTeams) {
            result.put(team.getShortName(), team);
        }
        return result;
    }

    private Map<Integer, User> loadUserMap() {
        List<User> allUsers = userDao.getAllUsers();
        Map<Integer, User> result = new HashMap<>();
        for (User user : allUsers) {
            result.put(user.getFaId(), user);
        }
        return result;
    }

    private Map<String, Country> loadCountryMap() {
        Map<String, Country> result = new HashMap<>();
        List<Country> countries = countryDao.getCountries();
        for (Country country : countries) {
            result.put(country.getName(), country);
        }
        return result;
    }

}
