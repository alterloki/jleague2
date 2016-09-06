package ru.jleague13.all;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.jleague13.Jleague2Application;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.User;
import ru.jleague13.repository.CountryDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ashevenkov 23.04.16 16:40.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AllParserTests {

    @Autowired
    private CountryDao countryDao;

    @Test
    public void testParseAllFromFile() throws Exception {
        Resource resource = new ClassPathResource("all13.zip");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        AllZip allZip = new AllParser(loadCountryMap()).readAll(br);
        //System.out.println(allZip.getTeams().size());
    }

    private Map<String, Country> loadCountryMap() {
        Map<String, Country> result = new HashMap<>();
        List<Country> countries = countryDao.getCountries();
        for (Country country : countries) {
            result.put(country.getName(), country);
        }
        return result;
    }

    private Map<Integer, User> loadUserMap() {
        //todo implement
        return new HashMap<>();
    }
}
