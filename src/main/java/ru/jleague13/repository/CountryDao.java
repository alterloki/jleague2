package ru.jleague13.repository;

import ru.jleague13.entity.Country;

import java.util.List;
import java.util.Map;

/**
 * @author ashevenkov 13.09.15 19:27.
 */
public interface CountryDao {

    Country getCountry(int countryId);

    List<Country> getCountries();

    Map<String, Country> getCountriesMap();

    void saveCountry(Country country);

    void deleteCountry(int countryId);

}
