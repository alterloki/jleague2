package ru.jleague13.calendar;

/**
 * @author ashevenkov 14.01.17 21:12.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ashevenkov 08.10.16 16:15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EFTest {

    @Autowired
    private EventFactory eventFactory;

    @Test
    public void testParseEvent() {
        assert true;
        Event event = eventFactory.createEvent("постройки/приглашения");
        assert event.getEventType() == EventType.BUILDINGS;
        assert event.getText().equals("постройки/приглашения");
        assert event.getPart() == 0;
        assert event.getTourNum() == 0;
        assert event.getMatchType() == MatchType.NONE;
        event = eventFactory.createEvent("кубки Лиг (1/16 финала - ответные игры)");
        assert event.getEventType() == EventType.LEAGUE_CUP;
        assert event.getText().equals("кубки Лиг (1/16 финала - ответные игры)");
        assert event.getPart() == 2;
        assert event.getTourNum() == 16;
        assert event.getMatchType() == MatchType.PLAYOFF;
    }
}
