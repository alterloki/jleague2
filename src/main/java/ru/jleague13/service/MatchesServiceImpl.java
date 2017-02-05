package ru.jleague13.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.download.DownloadInfo;
import ru.jleague13.entity.Match;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.CalendarEventsDao;
import ru.jleague13.repository.MatchDao;
import ru.jleague13.repository.TeamDao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 05.02.17 1:38.
 */
@Service
public class MatchesServiceImpl implements MatchesService {

    private Log log = LogFactory.getLog(MatchesServiceImpl.class);

    @Autowired
    private MatchDao matchDao;
    @Autowired
    private DownloadInfo downloadInfo;
    @Autowired
    private CalendarEventsDao calendarEventsDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private SeasonService seasonService;

    @Override
    public void downloadAndSaveRegular() {
        log.info("Started to download regular matches.");
        try {
            Date seasonStart = seasonService.getSeasonStart();
            Date seasonEnd = seasonService.getSeasonFinish();
            List<Match> matches = downloadInfo.downloadAllTournamentMatches();
            Map<Integer, Map<String, Match>> newMatchesMap = convertList(matches);
            List<Event> regularEvents = calendarEventsDao.loadCalendarEventsOfType(seasonStart, seasonEnd,
                    EventType.REGULAR_TOUR);
            for (Event regularEvent : regularEvents) {
                log.info("Merging matches for regular matches for date " + regularEvent.getDay());
                Map<String, Match> oldMatches = matchDao.loadMatches(regularEvent).stream().collect(Collectors.toMap(m -> m.getOwnerTeamId() + "|" + m.getGuestTeamId(),
                        Function.identity()));
                Map<String, Match> newMatches = newMatchesMap.get(regularEvent.getTourNum());
                if(newMatches != null) {
                    for (Map.Entry<String, Match> newEntry : newMatches.entrySet()) {
                        Match oldMatch = oldMatches.get(newEntry.getKey());
                        if (oldMatch != null) {
                            if (!oldMatch.equals(newEntry.getValue())) {
                                Match newM = newEntry.getValue();
                                newM.setId(oldMatch.getId());
                                matchDao.saveMatch(newM);
                            }
                            oldMatches.remove(newEntry.getKey());
                        } else {
                            matchDao.saveMatch(newEntry.getValue());
                        }
                    }
                    for (Match match : oldMatches.values()) {
                        matchDao.deleteMatch(match);
                    }
                }
            }
            log.info("Finished downloading and saving regular matches.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, Map<String, Match>> convertList(List<Match> matches) {
        Map<String, Team> name2Team =
                teamDao.getAllTeams().stream().collect(Collectors.toMap(Team::getName, Function.identity()));
        for (Match match : matches) {
            match.setOwnerTeamId(name2Team.get(match.getOwnerTeamName()).getId());
            match.setGuestTeamId(name2Team.get(match.getGuestTeamName()).getId());
        }
        return  matches.stream().collect(Collectors.groupingBy((p) -> p.getMatchEvent().getTourNum(),
                Collectors.toMap(m -> m.getOwnerTeamId() + "|" + m.getGuestTeamId(),
                        Function.identity())));
    }

}
