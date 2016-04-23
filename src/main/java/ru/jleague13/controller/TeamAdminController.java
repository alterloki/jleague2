package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.TeamDao;

/**
 * @author ashevenkov 15.09.15 0:41.
 */
@Controller
@RequestMapping("new/admin")
public class TeamAdminController {

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private CountryDao countryDao;

    @RequestMapping(value="/country/{country_id}/team", method = RequestMethod.GET)
    public String getCountryTeams(@PathVariable("country_id") int countryId, Model model) {
        model.addAttribute("country", countryDao.getCountry(countryId));
        model.addAttribute("teams", teamDao.getCountryTeams(countryId));
        return "admin/team";
    }

    @RequestMapping(value="/country/{country_id}/team", method = RequestMethod.PUT)
    public String createTeam(@PathVariable("country_id") int countryId, Model model) {
        teamDao.saveTeam(new Team(countryId));
        return "ok";
    }

    @RequestMapping(value="/country/{country_id}/team", method = RequestMethod.POST)
    public @ResponseBody Team saveOrUpdateTeam(@ModelAttribute Team team) {
        teamDao.saveTeam(team);
        return team;
    }

    @RequestMapping(value="/country/{country_id}/team", method = RequestMethod.POST, params="action=delete")
    public @ResponseBody Team deleteTeam(@ModelAttribute Team team) {
        teamDao.deleteTeam(team.getId());
        return team;
    }

    @RequestMapping(value="/country/{country_id}/team", method = RequestMethod.POST, params="action=restore")
    public @ResponseBody Team restoreTeam(@ModelAttribute Team team) {
        //TODO implement undelete?
        return team;
    }

}
