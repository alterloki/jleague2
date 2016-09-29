package ru.jleague13.download;

import ru.jleague13.all.AllZip;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;

import java.io.IOException;
import java.util.List;

/**
 * @author ashevenkov 18.09.15 23:42.
 */
public interface DownloadInfo {

    List<Country> downloadCountries() throws IOException;

    List<Team> downloadTeams(Country country) throws IOException;

    AllZip downloadAll() throws IOException;

    void downloadCurrentAllFile() throws IOException;
}
