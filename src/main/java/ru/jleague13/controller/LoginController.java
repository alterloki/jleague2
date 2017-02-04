package ru.jleague13.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.entity.User;
import ru.jleague13.mail.YandexMail;
import ru.jleague13.repository.UserDao;
import ru.jleague13.security.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 20.08.16 18:13.
 */
@Controller
public class LoginController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private YandexMail mail;

    @RequestMapping(value="/new/login", method = RequestMethod.GET)
    public String showSimpleForm(HttpServletRequest request, Model model) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        return "login";
    }

    @RequestMapping(value="/new/register", method = RequestMethod.GET)
    public String showRegisterForm(HttpServletRequest request, Model model) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);
        return "register";
    }

    @RequestMapping(value="/new/register", method = RequestMethod.POST)
    public String registerUser(HttpServletRequest request, Model model) {
        String name = request.getParameter("login");
        String email = request.getParameter("email");
        User userByLogin = userDao.getUserByLogin(name);
        if(userByLogin != null) {
            if(userByLogin.isRegistered()) {
                model.addAttribute("registerError", "already-registered");
            } else {
                if(userByLogin.getEmail().toLowerCase().equals(email.toLowerCase())) {
                    String password = generateAndSendPassword(email);
                    userByLogin.setPassword(password);
                    userByLogin.setRegistered(true);
                    userDao.saveUser(userByLogin);
                    return "redirect:/new/welcome";
                } else {
                    model.addAttribute("registerError", "wrong-email");
                }
            }
        } else {
            model.addAttribute("registerError", "no-fa-user");
        }
        return "register";
    }

    private String generateAndSendPassword(String email) {
        String pwd = RandomStringUtils.randomNumeric(6);
        mail.sendMail(email, "Регистрация на jleague", "Ваш пароль: " + pwd);
        return new BCryptPasswordEncoder().encode(pwd);
    }

    @RequestMapping(value="/new/welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome";
    }

    @RequestMapping(value="/new/login-error", method = RequestMethod.GET)
    public String showError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
