package ru.jleague13.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.TeamDao;

import java.util.List;

/**
 * @author ashevenkov 17.04.16 15:01.
 */
@Controller
public class TeamController {

    private Log log = LogFactory.getLog(TeamController.class);

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping(value="/teams", method = RequestMethod.GET)
    public String getTeams(Model model) {
        model.addAttribute("countries", countryDao.getCountries());
        model.addAttribute("topTeams", teamDao.getTopTeams(10));
        return "/teams";
    }

    @RequestMapping(value="/teams/sub/{substr}", method = RequestMethod.GET)
    public String getTeamsBySubstring(@PathVariable("substr") String substr, Model model) {
        if(substr.length() == 0) {
            model.addAttribute("teams", teamDao.getTopTeams(10));
        } else {
            model.addAttribute("teams", teamDao.getTeamsBySubstr(substr, 10));
        }
        return "/teamslist";
    }

    @RequestMapping(value="/teams/sub/", method = RequestMethod.GET)
    public String getTeamsByESubstring(Model model) {
        return getTeamsBySubstring("", model);
    }
}