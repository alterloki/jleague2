package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.download.DownloadInfo;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;
import ru.jleague13.repository.CountryDao;
import ru.jleague13.repository.TeamDao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 18.09.15 23:53.
 */
@Component
public class InformationManager {

    @Autowired
    private DownloadInfo downloadInfo;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private TeamDao teamDao;

    @Transactional
    public void updateCountries() throws IOException {
        Map<String, Country> countryMap =
                downloadInfo.downloadCountries().stream().
                        collect(Collectors.toMap(Country::getFaId, Function.<Country>identity()));
        Map<String, Country> currentMap =
                countryDao.getCountries().stream().
                        collect(Collectors.toMap(Country::getFaId, Function.<Country>identity()));
        for (String currentKey : currentMap.keySet()) {
            if(countryMap.containsKey(currentKey)) {
                Country country = countryMap.get(currentKey);
                Country currentCountry = currentMap.get(currentKey);
                updateCountry(currentCountry, country);
                updateCountryTeams(currentCountry);
                countryMap.remove(currentKey);
            } else {
                countryDao.deleteCountry(currentMap.get(currentKey).getId());
            }
        }
        for (String key : countryMap.keySet()) {
            Country country = countryMap.get(key);
            countryDao.saveCountry(country);
            updateCountryTeams(country);
        }
    }

    private void updateCountry(Country currentCountry, Country country) {
        if(!country.equals(currentCountry))     {
            countryDao.saveCountry(country);
        }
    }

    public void updateCountryTeams(Country country) throws IOException {
        Map<String, Team> teamMap =
                downloadInfo.downloadTeams(country).stream().
                        collect(Collectors.toMap(Team::getShortName, Function.<Team>identity()));
        Map<String, Team> currentMap =
                teamDao.getCountryTeams(country.getId()).stream().
                        collect(Collectors.toMap(Team::getShortName, Function.<Team>identity()));
        for (String currentKey : currentMap.keySet()) {
            if(teamMap.containsKey(currentKey)) {
                Team team = teamMap.get(currentKey);
                Team currentTeam = currentMap.get(currentKey);
                updateTeam(currentTeam, team);
                teamMap.remove(currentKey);
            } else {
                teamDao.deleteTeam(currentMap.get(currentKey).getId());
            }
        }
        for (String key : teamMap.keySet()) {
            Team team = teamMap.get(key);
            teamDao.saveTeam(team);
        }
    }

    private void updateTeam(Team currentTeam, Team team) {
        if(!team.equals(currentTeam)) {
            if(currentTeam.getId() > 0) {
                team.setId(currentTeam.getId());
            }
            teamDao.saveTeam(team);
        }
    }

}
