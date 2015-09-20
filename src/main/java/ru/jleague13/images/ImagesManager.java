package ru.jleague13.images;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;

import java.io.File;

/**
 * @author ashevenkov 18.09.15 15:46.
 */
@Component
public class ImagesManager {

    public static final String IMAGES_URL = "/dimages";

    @Value("${images.dir}")
    private String imagesDir;

    public String countryFlagFile(Country country) {
        return imagesDir + flagImageName(country);
    }

    public String countryFlagUrl(Country country) {
        return IMAGES_URL + flagImageName(country);
    }

    private String flagImageName(Country country) {
        return "/country/" + country.getId() + "_flag.jpg";
    }

    public String teamEmblemFile(Team team) {
        return imagesDir + emblemImageName(team);
    }

    public String teamEmblemUrl(Team team) {
        return IMAGES_URL + emblemImageName(team);
    }

    private String emblemImageName(Team team) {
        return "/team/" + team.getId() + "_emblem.jpg";
    }

    public void deleteFlagFile(Country country) {
        //todo return result
        new File(countryFlagFile(country)).delete();
    }

    public void deleteTeamEmblem(Team team) {
        //todo return result
        new File(teamEmblemFile(team)).delete();
    }
}
