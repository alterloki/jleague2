package ru.jleague13.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.ProgressType;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.ProgressDao;

import java.io.IOException;

/**
 * @author ashevenkov 15.09.15 0:41.
 */
@Controller
@RequestMapping("new/admin/country")
public class CountryController {

    private Log log = LogFactory.getLog(CountryController.class);

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private InformationManager informationController;
    @Autowired
    private ProgressDao progressDao;

    @RequestMapping(method = RequestMethod.GET)
    public String adminMain(Model model) {
        model.addAttribute("countries", countryDao.getCountries());
        boolean progress = progressDao.getProgress(ProgressType.COUNTRY_DOWNLOAD);
        model.addAttribute("progress", progress);
        return "admin/country";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String createCountry() {
        countryDao.saveCountry(new Country());
        return "ok";
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Country updateCountry(@ModelAttribute Country country) {
        countryDao.saveCountry(country);
        return country;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String downloadAndUpdate(@ModelAttribute Country country) {
        try {
            progressDao.startProgress(ProgressType.COUNTRY_DOWNLOAD);
            informationController.updateCountries();
            progressDao.finishProgress(ProgressType.COUNTRY_DOWNLOAD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @RequestMapping(method = RequestMethod.POST, params = "action=delete")
    public
    @ResponseBody
    Country deleteCountry(@ModelAttribute Country country) {
        countryDao.deleteCountry(country.getId());
        return country;
    }

    @RequestMapping(method = RequestMethod.POST, params = "action=restore")
    public
    @ResponseBody
    Country restoreCountry(@ModelAttribute Country country) {
        //TODO implement undelete?
        return country;
    }
}
