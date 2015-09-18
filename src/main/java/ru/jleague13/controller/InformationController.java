package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.download.DownloadInfo;
import ru.jleague13.entity.Country;
import ru.jleague13.repository.CountryDao;

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
public class InformationController {

    @Autowired
    private DownloadInfo downloadInfo;
    @Autowired
    private CountryDao countryDao;

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
                countryMap.remove(currentKey);
            } else {
                countryDao.deleteCountry(currentMap.get(currentKey).getId());
            }
        }
        for (String key : countryMap.keySet()) {
            countryDao.saveCountry(countryMap.get(key));
        }
    }

    private void updateCountry(Country currentCountry, Country country) {
        if((country.getFaIndex() != null && !country.getFaIndex().equals(currentCountry.getFaIndex()))||
                (country.getName() != null && !country.getName().equals(currentCountry.getName())))     {
            countryDao.saveCountry(country);
        }
    }

}
