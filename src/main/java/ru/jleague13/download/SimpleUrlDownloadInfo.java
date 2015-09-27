package ru.jleague13.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;
import ru.jleague13.entity.User;
import ru.jleague13.repository.UserDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashevenkov 19.09.15 0:09.
 */
@Component
public class SimpleUrlDownloadInfo implements DownloadInfo {

    @Autowired
    private FaUrlResolver faUrlResolver;
    @Autowired
    private UserDao userDao;

    @Override
    public List<Country> downloadCountries() throws IOException {
        ArrayList<Country> countries = new ArrayList<>();
        Document doc = Jsoup.connect(faUrlResolver.getFa13Countries()).get();
        Elements select = doc.select("div[id=team]").select("tr[class^=table]");
        for(int i = 0; i < select.size(); i++) {
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
        for(int i = 0; i < select.size(); i++) {
            String shortName = select.get(i).select("a[href^=team]").attr("href").substring(15);
            String name = select.get(i).select("a[href^=team]").select("b").text();
            Team team = new Team(0, shortName, name, country.getId());
            String managerLogin = select.get(i).select("a[href^=profile]").text();
            String managerIdString = select.get(i).select("a[href^=profile]").attr("href").substring(16);
            if(managerIdString.length() > 0) {
                int managerId = Integer.parseInt(managerIdString);
                User user = userDao.getUserByFaId(managerId);
                team.setManagerLogin(managerLogin);
                if(user != null) {
                    team.setManagerId(user.getId());
                } else {
                    int userId = userDao.saveUser(new User(0, managerLogin, managerId));
                    team.setManagerId(userId);
                }
            }
            teams.add(team);
        }
        return teams;
    }
}
