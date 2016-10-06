package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.all.AllInfo;
import ru.jleague13.all.AllManager;
import ru.jleague13.repository.AllDao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 05.10.16 18:00.
 */
@Controller
@RequestMapping("/new/admin/all")
public class AllController {

    @Autowired
    private AllDao allDao;
    @Autowired
    private AllManager allManager;

    @RequestMapping(method = RequestMethod.GET)
    public String getAllZips(Model model) {
        List<AllInfo> registered = allDao.getRegistered();
        model.addAttribute("all_files", registered);
        return "admin/all";
    }

    @RequestMapping(value="/{id}", method = RequestMethod.POST)
    public String parseAllZip(@PathVariable("id") int allId) {
        try {
            AllInfo allInfo = allDao.getById(allId);
            allManager.parseAndUpdateAll(allInfo.getAllDate());
        } catch (IOException e) {
            return "error";
        }
        return "ok";
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public String refreshRegister() {
        allManager.refreshAllRegister();
        return "redirect:/new/admin/all";
    }
}
