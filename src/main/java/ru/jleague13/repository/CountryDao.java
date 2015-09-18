package ru.jleague13.repository;

import ru.jleague13.entity.Country;

import java.util.List;

/**
 * @author ashevenkov 13.09.15 19:27.
 */
public interface CountryDao {

    Country getCountry(int countryId);

    List<Country> getCountries();

    void saveCountry(Country country);

    void deleteCountry(int countryId);

}
