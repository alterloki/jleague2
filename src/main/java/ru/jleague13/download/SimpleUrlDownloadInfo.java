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
        Elements select = doc.select("section[class=l-content-frame]").select("table").select("tr");
        for (int i = 1; i < select.size(); i++) {
            String name = select.get(i).select("td:eq(1)").text();
            String faIndex = select.get(i).select("a").attr("href").substring(19);
            countries.add(new Country(0, null, name, faIndex));
        }
        return countries;
    }

    @Override
    public List<Team> downloadTeams(Country country) throws IOException {
        ArrayList<Team> teams = new ArrayList<>();
        Document doc = Jsoup.connect(faUrlResolver.getFa13Teams(country)).get();
        Elements select = doc.select("section[class=l-content-frame]").select("table");
        for(int div = 1; div <= select.size(); div++) {
            Element table = select.get(div - 1);
            Elements lines = table.select("tr");
            for (int i = 1; i < lines.size(); i++) {
                String shortName = lines.get(i).select("td:eq(1)").select("a").attr("href").substring(6);
                String name = lines.get(i).select("td:eq(1)").select("a").text();
                Team team = new Team(0, shortName, name, country.getId());
                team.setDiv(div);
                String managerLogin = lines.get(i).select("td:eq(4)").select("a").text();
                if(!managerLogin.equals("Отправить заявку")) {
                    String managerIdString = lines.get(i).select("td:eq(4)").select("a").
                            attr("href").substring(9);
                    int managerId = Integer.parseInt(managerIdString);
                    User user = userDao.getUserByFaId(managerId);
                    team.setManagerId(managerId);
                    team.setManagerLogin(managerLogin);
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

    @Override
    public List<Match> downloadTournamentMatches(String tournamentFaIndex) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        List<Match> result = new ArrayList<>();
        String url = faUrlResolver.getTournamentMatchesUrl(tournamentFaIndex);
        Document doc = Jsoup.connect(url).get();
        Elements tables = doc.select("section[class=l-content-frame]").select("table");
        Elements headers = doc.select("section[class=l-content-frame]").select("h3");
        for(int i = 0; i < tables.size(); i++) {
            String tourDate = headers.get(i).select("span").text().substring(2);
            Date date = dateFormat.parse(tourDate);
            Elements lines = tables.get(i).select("tr");
            for(int j = 1; j < lines.size(); j++) {
                String score = lines.get(j).select("td:eq(0)").text();
                Elements commands = lines.get(j).select("td:eq(1)").select("a");
                String nameOwner = commands.get(0).text();
                String nameGuest = commands.get(1).text();
                int owner = -1;
                int guest = -1;
                if(score.length() > 0) {
                    String[] parts = score.split(":");
                    owner = Integer.parseInt(parts[0]);
                    guest = Integer.parseInt(parts[1]);
                }
                Event matchEvent = new Event(EventType.REGULAR_TOUR, "", i + 1, 0);
                matchEvent.setDay(date);
                result.add(new Match(0, 0, nameOwner, 0,
                        nameGuest, owner, guest, matchEvent));
            }
        }
        return result;
    }

    @Override
    public List<Match> downloadAllTournamentMatches() throws IOException, ParseException {
        log.info("Started to download regular matches pages.");
        List<Match> result = new ArrayList<>();
        String url = faUrlResolver.getRegularTournamensUrl();
        Document doc = Jsoup.connect(url).get();
        Elements lines = doc.select("section[class=l-content-frame]").select("table").select("tr");
        for(int i = 1; i < lines.size(); i++) {
            Elements cells = lines.get(i).select("td");
            for(int j = 1; j < cells.size(); j++) {
                String tournamentLink = cells.get(j).select("a").attr("href");
                if(tournamentLink != null && tournamentLink.length() > 0) {
                    String tournamentIndex = tournamentLink.substring(20);
                    result.addAll(downloadTournamentMatches(tournamentIndex));
                }
            }
        }
        log.info("Regular matches pages downloaded. Matches size = " + result.size());
        return result;
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
