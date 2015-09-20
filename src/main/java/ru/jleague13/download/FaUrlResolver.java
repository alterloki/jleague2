package ru.jleague13.download;

import org.springframework.stereotype.Component;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;

/**
 * @author ashevenkov 15.09.15 0:37.
 */
@Component
public class FaUrlResolver {

    public static final String FA13_URL = "http://fa13.info";

    public String getFaCountryFlag(Country country) {
        return FA13_URL + "/image/flag_s/flag_s_" + country.getFaIndex() + ".gif";
    }

    public String getFaTeamEmblem(Team team) {
        return FA13_URL + "/image/logo/" + team.getShortName() + ".gif";
    }

    public String getFa13Countries() {
        return FA13_URL + "/team.html";
    }

    public String getFa13Teams(Country country) {
        return FA13_URL + "/team.html?country=" + country.getFaId();
    }
}
