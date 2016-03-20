package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.repository.UserDao;

/**
 * @author ashevenkov 19.03.16 15:27.
 */
@Controller
@RequestMapping("new/admin/users")
public class UsersController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public String showUsers(Model model) {
        return showRegisteredUsers(model);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String showAllUsers(Model model) {
        model.addAttribute("users", userDao.getAllUsers());
        model.addAttribute("filter", "all");
        return "admin/users";
    }

    @RequestMapping(value = "/registered", method = RequestMethod.GET)
    public String showRegisteredUsers(Model model) {
        model.addAttribute("users", userDao.getRegisteredUsers());
        model.addAttribute("filter", "registered");
        return "admin/users";
    }
}
