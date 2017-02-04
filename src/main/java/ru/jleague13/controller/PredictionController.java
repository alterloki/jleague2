package ru.jleague13.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Match;
import ru.jleague13.prediction.MatchesList;
import ru.jleague13.prediction.PredictionService;
import ru.jleague13.repository.TeamDao;
import ru.jleague13.security.SecurityService;

import java.util.List;

/**
 * @author ashevenkov 28.01.17 15:35.
 */
@Controller
public class PredictionController {

    private Log log = LogFactory.getLog(PredictionController.class);

    @Autowired
    private PredictionService predictionService;
    @Autowired
    private TeamDao teamDao;

    @RequestMapping(value = "/prediction", method = RequestMethod.GET)
    public String predictionTable(Model model) {
        model.addAttribute("japanTeams", teamDao.getJapanLiveTeams());
        List<Match> matchesForUser =
                predictionService.getPredictionMatchesForUser(EventType.REGULAR_TOUR);
        model.addAttribute("matches", matchesForUser);
        model.addAttribute("usersTable", predictionService.getPredictionUsersTable());
        return "prediction";
    }

    @RequestMapping(value = "/prediction/make", method = RequestMethod.GET)
    public String makePrediction(Model model) {
        model.addAttribute("japanTeams", teamDao.getJapanLiveTeams());
        List<Match> matchesForUser =
                predictionService.getPredictionMatchesForUser(EventType.REGULAR_TOUR);
        for (Match match : matchesForUser) {
            log.info("Match prev = " + match.getId());
        }
        model.addAttribute("matchesList", new MatchesList(matchesForUser));
        model.addAttribute("usersTable", predictionService.getPredictionUsersTable());
        return "make-prediction";
    }

    @RequestMapping(value = "/prediction/save", method = RequestMethod.POST)
    public String savePrediction(Model model, MatchesList matchesList) {
        List<Match> matches = matchesList.getMatches();
        for (Match match : matches) {
            log.info("Match = " + match.getId());
        }
        predictionService.savePrediction(matches);
        return "redirect:/prediction";
    }
}
