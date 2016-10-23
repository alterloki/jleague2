package ru.jleague13.all;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jleague13.controller.InformationManager;
import ru.jleague13.download.FaUrlResolver;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.FaUser;
import ru.jleague13.entity.Team;
import ru.jleague13.entity.User;
import ru.jleague13.repository.AllDao;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.TeamDao;
import ru.jleague13.repository.UserDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 30.09.16 15:37.
 */
@Component
public class AllManager {

    private Log log = LogFactory.getLog(AllManager.class);

    @Value("${download.dir}")
    private String downloadDir;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private FaUrlResolver faUrlResolver;
    @Autowired
    private AllDao allDao;
    @Autowired
    private InformationManager informationManager;
    @Autowired
    private UserDao userDao;

    public String getAllFileName(Date date) {
        String dateString = new SimpleDateFormat("dd_MM_yyyy").format(date);
        return downloadDir + "/all/" + dateString + "_all.zip";
    }

    public void registerAllFile(Date date) {
        allDao.registerAll(date);
    }

    public void parseAndUpdateAll(Date date) throws IOException {
        AllInfo lastAll = allDao.getLastAll();
        AllInfo currentAllInfo = allDao.getAllInfoOnDate(date);
        List<Country> countriesList = countryDao.getCountries();
        Map<String, Country> name2Country = countriesList.stream().collect(Collectors.toMap(Country::getName,
                Function.<Country>identity()));
        Map<Integer, User> faId2User = userDao.getAllUsers().stream().collect(Collectors.toMap(User::getFaId,
                Function.<User>identity()));
        AllParser allParser = new AllParser(name2Country, faId2User);
        FileInputStream fis = new FileInputStream(getAllFileName(date));
        AllZip allZip = allParser.readAll(fis);
        Map<Integer, FaUser> usersMap = allZip.getUsersMap();
        List<Team> teams = allZip.getTeams();
        Map<Integer, Country> id2Country = countriesList.stream().collect(Collectors.toMap(Country::getId,
                Function.<Country>identity()));
        Map<Integer, List<Team>> countryId2Team = teams.stream().
                collect(Collectors.groupingBy(Team::getCountryId));
        informationManager.updateAllTeamInfo(currentAllInfo.getId(), countryId2Team);
        if (lastAll.getAllDate().equals(date)) {
            for (Map.Entry<Integer, FaUser> entry : usersMap.entrySet()) {
                userDao.saveFaUser(entry.getValue());
            }
            for (Map.Entry<Integer, List<Team>> entry : countryId2Team.entrySet()) {
                informationManager.updateCountryTeams(id2Country.get(entry.getKey()),
                        entry.getValue().stream().collect(
                                Collectors.toMap(Team::getShortName, Function.<Team>identity())));
            }
        }
        allDao.makeParsed(date);
    }

    public void downloadCurrentAllFile() throws IOException {
        URL url = new URL(faUrlResolver.getAllZip());
        byte[] allBytes = ByteStreams.toByteArray(url.openStream());
        Date date = new Date();
        File to = new File(getAllFileName(date));
        log.info("File = " + to.getAbsolutePath());
        Files.createParentDirs(to);
        Files.write(allBytes, to);
        registerAllFile(date);
    }

    public void refreshAllRegister() {
        try {
            List<AllInfo> registeredAll = allDao.getRegistered();
            Set<Date> registeredDates = new HashSet<>();
            for (AllInfo allInfo : registeredAll) {
                registeredDates.add(allInfo.getAllDate());
            }
            File allDir = new File(downloadDir + "/all/");
            File[] allFiles = allDir.listFiles();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
            for (File allFile : allFiles) {
                String dateString = allFile.getName().substring(0, 10);
                Date curDate = dateFormat.parse(dateString);
                if (!registeredDates.contains(curDate)) {
                    allDao.registerAll(curDate);
                }
            }
        } catch (ParseException e) {
            log.error("Date from file parse error.", e);
        }
    }
}
