package ru.jleague13.download;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.jleague13.Jleague2Application;
import ru.jleague13.entity.Country;

import java.io.IOException;
import java.util.List;

/**
 * @author ashevenkov 19.09.15 0:17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Jleague2Application.class)
public class DownloadInfoTests {

    @Autowired
    private DownloadInfo downloadInfo;

    @Test
    public void testDownloadCountries() throws IOException {
        List<Country> countries = downloadInfo.downloadCountries();
        assert countries.size() == 34;
        boolean wasJapan = false;
        for (Country country : countries) {
            if(country.getFaIndex().equals("JPN")) {
                wasJapan = true;
                assert country.getName().equals("Япония");
                assert country.getFaId().equals("209");
            }
        }
        assert wasJapan;
    }
}
