package ru.jleague13.download;

import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * @author ashevenkov 13.09.15 23:49.
 */
public interface DownloadImages {

    void downloadFlag(Country country) throws IOException;

    void downloadEmblem(Team team) throws IOException;
}
