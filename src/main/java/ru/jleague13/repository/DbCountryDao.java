package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.jleague13.download.DownloadImages;
import ru.jleague13.entity.Country;
import ru.jleague13.images.ImagesManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ashevenkov 15.09.15 0:52.
 */
@Repository
public class DbCountryDao implements CountryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private ImagesManager imagesManager;
    @Autowired
    private DownloadImages downloadImages;

    @Override
    public Country getCountry(int countryId) {
        return jdbcTemplate.query(
                "select id, fa_id, fa_index, name from country where id = ?",
                    rs -> {
                        if(rs.next()) {
                            return fromResultSet(rs);
                        } else {
                            return null;
                        }
                    }, countryId);
    }

    @Override
    public List<Country> getCountries() {
        return jdbcTemplate.query(
                "select id, fa_id, fa_index, name from country",
                (rs, i) -> fromResultSet(rs));
    }

    private Country fromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        Country country = new Country(
                id,
                resultSet.getString("fa_id"),
                resultSet.getString("name"),
                resultSet.getString("fa_index"));
        country.setPicture(imagesManager.countryFlagUrl(country));
        return country;
    }

    @Override
    public void saveCountry(Country country) {
        if(country.getId() > 0) {
            Country oldCountry = getCountry(country.getId());
            if(country.getFaIndex() != null &&
                    !country.getFaIndex().equals(oldCountry.getFaIndex())) {
                try {
                    downloadImages.downloadFlag(country);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
            jdbcTemplate.update("update country set fa_id = ?, name = ?, fa_index = ? where id = ?",
                    country.getFaId(), country.getName(), country.getFaIndex(), country.getId());
        } else {
            jdbcTemplate.update("insert into country (fa_id, name, fa_index) values (?, ?, ?)",
                    country.getFaId(), country.getName(), country.getFaIndex());
        }
    }

    @Override
    public void deleteCountry(int countryId) {
        Country country = getCountry(countryId);
        imagesManager.deleteFlagFile(country);
        teamDao.deleteCountryTeams(countryId);
        jdbcTemplate.update("delete from country where id = ?", countryId);
    }
}