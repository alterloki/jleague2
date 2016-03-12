package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.repository.UserDao;

/**
 * @author ashevenkov 15.11.15 20:58.
 */
@Controller
@RequestMapping("new/admin/users")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public String showUsers(Model model) {
        model.addAttribute("users", userDao.getAllUsers());
        return "admin/users";
    }

    @RequestMapping(value="/{user_id}", method = RequestMethod.GET)
    public String showUser(@PathVariable("user_id") int userId, Model model) {
        model.addAttribute("user", userDao.getUser(userId));
        return "admin/user";
    }
}
