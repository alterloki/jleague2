package ru.jleague13.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 20.08.16 18:13.
 */
@Controller
public class LoginController {

    @RequestMapping(value="/new/login", method = RequestMethod.GET)
    public String showSimpleForm(Model model) {
        return "login";
    }

    @RequestMapping(value="/new/login-error", method = RequestMethod.GET)
    public String showError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
