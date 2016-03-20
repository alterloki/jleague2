package ru.jleague13.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.entity.User;
import ru.jleague13.repository.UserDao;

/**
 * @author ashevenkov 15.11.15 20:58.
 */
@Controller
@RequestMapping("new/admin/user")
public class UserController {

    private Log log = LogFactory.getLog(UserController.class);

    @Autowired
    private UserDao userDao;

    @RequestMapping(value="/{user_id}", method = RequestMethod.GET)
    public String showUser(@PathVariable("user_id") int userId, Model model) {
        model.addAttribute("user", userDao.getUser(userId));
        return "admin/user";
    }

    @RequestMapping(value="/{user_id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("user_id") int userId, Model model) {
        userDao.deleteUser(userId);
        return "ok";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String addUser(Model model) {
        User user = new User();
        int id = userDao.saveUser(user);
        user.setId(id);
        model.addAttribute("user", user);
        return "admin/user";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addUser(User user, Model model) {
        userDao.saveUser(user);
        return "redirect:/new/admin/user/" + user.getId();
    }

}
