package ru.jleague13.prediction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.Jleague2Application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author ashevenkov 26.09.15 0:33.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Jleague2Application.class)
@Transactional
public class PredictionTests {

    @Test
    public void testSaveTransfer() throws FileNotFoundException, ParseException {

    }
}
