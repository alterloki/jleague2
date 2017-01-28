package ru.jleague13.download;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jleague13.all.AllManager;
import ru.jleague13.all.AllParser;
import ru.jleague13.all.AllZip;
import ru.jleague13.calendar.*;
import ru.jleague13.calendar.Calendar;
import ru.jleague13.entity.*;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.TeamDao;
import ru.jleague13.repository.UserDao;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author ashevenkov 19.09.15 0:09.
 */
@Component
public class SimpleUrlDownloadInfo implements DownloadInfo {

    private Log log = LogFactory.getLog(SimpleUrlDownloadImages.class);

    @Autowired
    private FaUrlResolver faUrlResolver;
    @Autowired
    private UserDao userDao;
    @Autowired
    private EventFactory eventFactory;
    @Value("${download.dir}")
    private String downloadDir;


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
    public Calendar downloadCalendar(String startFrom) throws IOException {
        try {
            Calendar result = new Calendar();
            String url = faUrlResolver.getCalendarUrl() + startFrom;
            while(url != null) {
                Document doc = Jsoup.connect(url).get();
                int count = parsePage(doc, result);
                if(count > 0) {
                    String nextUrl = doc.select(".link-next").attr("href");
                    url = FaUrlResolver.FA13_URL + nextUrl;
                } else {
                    url = null;
                }
            }
            return result;
        } catch (ParseException e) {
            log.error("Error parsing date.", e);
            return null;
        }
    }

    private int parsePage(Document doc, Calendar result) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        String monthString = doc.select("#select-month > option[selected]").attr("value");
        log.info("Month = " + monthString);
        Elements select = doc.select(".calendar > tbody > tr");
        log.info("Selected size = " + select.size());
        int sum = 0;
        for (int i = 0; i < select.size(); i++) {
            String dayString = select.get(i).select("td:eq(0)").text().
                    trim().split(",")[0];
            Date date = dateFormat.parse(dayString + "-" + monthString);
            Elements lines = select.get(i).select("td:eq(1) > div");
            HashSet<Event> events = new HashSet<>();
            for (Element line : lines) {
                if(!line.text().contains("тест")) {
                    String[] parts = line.text().split(",");
                    for (String part : parts) {
                        String[] parts1 = part.split(" и ");
                        for (String s : parts1) {
                            Event event = eventFactory.createEvent(s);
                            events.add(event);
                        }
                    }
                } else {
                    Event event = eventFactory.createEvent(line.text());
                    events.add(event);
                }
            }
            result.addDay(new CalendarDay(date, events));
            sum += events.size();
        }
        return sum;
    }


}
