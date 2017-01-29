package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Match;
import ru.jleague13.prediction.PredictionService;

import java.util.List;

/**
 * @author ashevenkov 28.01.17 15:35.
 */
@Controller
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @RequestMapping(value = "/prediction", method = RequestMethod.GET)
    public String predictionTable(Model model) {
        List<Match> matchesForUser =
                predictionService.getPredictionMatchesForUser(EventType.REGULAR_TOUR);
        model.addAttribute("matches", matchesForUser);
        model.addAttribute("usersTable", predictionService.getPredictionUsersTable());
        return "prediction";
    }
}
