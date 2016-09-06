package ru.jleague13.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @author ashevenkov 10.09.15 21:58.
 */
@Controller
@RequestMapping("/new/admin")
public class AdminController {

    @RequestMapping(method = RequestMethod.GET)
    public String admin() throws IOException {
        return "admin/news";
    }
}
