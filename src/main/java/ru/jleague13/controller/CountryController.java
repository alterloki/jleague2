package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.jleague13.entity.Country;
import ru.jleague13.images.ImagesManager;
import ru.jleague13.repository.CountryDao;

/**
 * @author ashevenkov 15.09.15 0:41.
 */
@Controller
@RequestMapping("/admin/country")
public class CountryController {

    @Autowired
    private CountryDao countryDao;

    @RequestMapping(method = RequestMethod.GET)
    public String adminMain(Model model) {
        model.addAttribute("countries", countryDao.getCountries());
        return "admin/country";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String createCountry() {
        countryDao.saveCountry(new Country());
        return "admin/country";
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    Country updateCountry(@ModelAttribute Country country) {
        countryDao.saveCountry(country);
        return country;
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
