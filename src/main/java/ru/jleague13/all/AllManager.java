package ru.jleague13.all;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.CountryDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 30.09.16 15:37.
 */
@Component
public class AllManager {

    @Value("${download.dir}")
    private String downloadDir;
    @Autowired
    private CountryDao countryDao;

    public String getAllFileName(Date date) {
        String dateString = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        return downloadDir + "/all/" + dateString + "_all.zip";
    }

    public void registerAllFile(Date date) {
        //todo implement
    }

    public void parseAndUpdateAll(Date date) throws IOException {
        AllParser allParser = new AllParser(countryDao.getCountriesMap());
        FileInputStream fis = new FileInputStream(getAllFileName(date));
        AllZip allZip = allParser.readAll(fis);
        List<Team> teams = allZip.getTeams();
    }
}
