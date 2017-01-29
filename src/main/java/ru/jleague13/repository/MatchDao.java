package ru.jleague13.repository;

import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.entity.Match;

import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 28.01.17 16:03.
 */
public interface MatchDao {

    List<Match> loadMatches(Event event);

    int saveMatch(Match match);
}
