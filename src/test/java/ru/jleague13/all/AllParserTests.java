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
import ru.jleague13.entity.FaUser;
import ru.jleague13.entity.User;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.UserDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 23.04.16 16:40.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AllParserTests {

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private UserDao userDao;

    @Test
    public void testParseAllFromFile() throws Exception {
        Resource resource = new ClassPathResource("all13.zip");
        Map<String, Country> countryMap = countryDao.getCountries().stream().collect(Collectors.toMap(Country::getName,
                Function.<Country>identity()));
        Map<Integer, User> userMap = userDao.getAllUsers().stream().collect(Collectors.toMap(User::getFaId,
                Function.<User>identity()));
        AllZip allZip = new AllParser(countryMap, userMap).readAll(resource.getInputStream());
        System.out.println(allZip.getTeams().size());
    }

}
