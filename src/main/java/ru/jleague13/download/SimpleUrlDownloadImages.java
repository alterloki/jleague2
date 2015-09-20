package ru.jleague13.download;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;
import ru.jleague13.images.ImagesManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author ashevenkov 16.09.15 15:02.
 */
@Component
public class SimpleUrlDownloadImages implements DownloadImages {

    @Autowired
    private FaUrlResolver urlResolver;

    @Autowired
    private ImagesManager imagesManager;

    @Override
    public void downloadFlag(Country country) throws IOException {
        URL url = new URL(urlResolver.getFaCountryFlag(country));
        byte[] pictureBytes = ByteStreams.toByteArray(url.openStream());
        File to = new File(imagesManager.countryFlagFile(country));
        Files.createParentDirs(to);
        Files.write(pictureBytes, to);
    }

    @Override
    public void downloadEmblem(Team team) throws IOException {
        URL url = new URL(urlResolver.getFaTeamEmblem(team));
        byte[] pictureBytes = ByteStreams.toByteArray(url.openStream());
        File to = new File(imagesManager.teamEmblemFile(team));
        Files.createParentDirs(to);
        Files.write(pictureBytes, to);
    }
}
