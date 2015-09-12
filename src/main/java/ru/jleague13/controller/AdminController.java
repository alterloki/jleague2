package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.TeamDao;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * @author ashevenkov 10.09.15 21:58.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TeamDao teamDao;

    @RequestMapping(method = RequestMethod.GET)
    public String adminMain(Model model) {
        model.addAttribute("teams", teamDao.getTeams());
        return "admin";
    }

    @RequestMapping(value = "/team", method = RequestMethod.POST)
    public @ResponseBody Team  updateTeam(@ModelAttribute Team team) {
        teamDao.saveTeam(team);
        return team;
    }

    @RequestMapping(value = "/team", method = RequestMethod.POST, params="action=delete")
    public @ResponseBody Team deleteTeam(@ModelAttribute Team team) {
        teamDao.deleteTeam(team.getId());
        return team;
    }

    @RequestMapping(value = "/team", method = RequestMethod.POST, params="action=restore")
    public @ResponseBody Team restoreTeam(@ModelAttribute Team team) {
        //teamDao.deleteTeam(team.getId());
        return team;
    }

    @RequestMapping(value = "/team", method = RequestMethod.PUT)
    public String deleteTeam() {
        teamDao.saveTeam(new Team());
        return "admin";
    }
}
