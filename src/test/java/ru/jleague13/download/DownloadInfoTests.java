package ru.jleague13.download;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.jleague13.Jleague2Application;
import ru.jleague13.all.AllZip;
import ru.jleague13.calendar.Calendar;
import ru.jleague13.calendar.CalendarDay;
import ru.jleague13.calendar.Event;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author ashevenkov 19.09.15 0:17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DownloadInfoTests {

    private Log log = LogFactory.getLog(DownloadInfoTests.class);

    @Autowired
    private DownloadInfo downloadInfo;

    @Test
    public void testDownloadCountries() throws IOException {
        List<Country> countries = downloadInfo.downloadCountries();
        assert countries.size() == 34;
        boolean wasJapan = false;
        for (Country country : countries) {
            if(country.getFaIndex().equals("JPN")) {
                wasJapan = true;
                assert country.getName().equals("Япония");
                assert country.getFaId() == null;
            }
        }
        assert wasJapan;
    }

    @Test
    public void testDownloadTeams() throws IOException {
        Country country = new Country(0, "0", "Япония", "JPN");
        List<Team> teams = downloadInfo.downloadTeams(country);
        boolean wasAkita = false;
        for (Team team : teams) {
            if(team.getName().equals("Блаублитз Акита")) {
                wasAkita = true;
                assert team.getShortName().equals("N16");
                assert team.getDiv() == 1;
                assert team.getManagerLogin().equals("alterloki");
            }
        }
        assert wasAkita;
        assert teams.size() == 32;
    }

    @Test
    public void testDownloadCalendar() throws IOException {
        Calendar calendar = downloadInfo.downloadCalendar("");
        assert calendar != null;
        Map<Date, CalendarDay> days = calendar.getDays();
        for (Map.Entry<Date, CalendarDay> entry : days.entrySet()) {
            log.info(entry.getKey() + " - ");
            Set<Event> events = entry.getValue().getEvents();
            for (Event event : events) {
                log.info(" - " + event);
            }
        }
    }
}
