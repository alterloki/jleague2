package ru.jleague13.download;

import ru.jleague13.all.AllZip;
import ru.jleague13.calendar.Calendar;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Match;
import ru.jleague13.entity.Team;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author ashevenkov 18.09.15 23:42.
 */
public interface DownloadInfo {

    List<Country> downloadCountries() throws IOException;

    List<Team> downloadTeams(Country country) throws IOException;

    Calendar downloadCalendar(String startFrom) throws IOException;

    List<Match> downloadTournamentMatches(String countryFaIndex) throws IOException, ParseException;

    List<Match> downloadAllTournamentMatches() throws IOException, ParseException;
}
