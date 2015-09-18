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

    @RequestMapping(method = RequestMethod.GET)
    public void admin() {
    }
}
