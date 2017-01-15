package ru.jleague13.download;

import org.springframework.stereotype.Component;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;

/**
 * @author ashevenkov 15.09.15 0:37.
 */
@Component
public class FaUrlResolver {

    public static final String OLD_FA13_URL = "http://old.fa13.info";
    public static final String REPOSITORY_FA13_URL = "http://repository.fa13.info";
    public static final String FA13_URL = "http://www.fa13.info";


    public String getFaCountryFlag(Country country) {
        return OLD_FA13_URL + "/image/flag_s/flag_s_" + country.getFaIndex() + ".gif";
    }

    public String getFaTeamEmblem(Team team) {
        return OLD_FA13_URL + "/image/logo/" + team.getShortName() + ".gif";
    }

    public String getFa13Countries() {
        return OLD_FA13_URL + "/team.html";
    }

    public String getFa13Teams(Country country) {
        return OLD_FA13_URL + "/team.html?country=" + country.getFaId();
    }

    public String getAllZip() {
        return REPOSITORY_FA13_URL + "/site/build/all13.zip";
    }

    public String getCalendarUrl() {
        return FA13_URL + "/calendar";
    }

    public String getTransferListUrl() {
        return FA13_URL + "/transfer/TList";
    }

    public String getTransferResultUrl() {
        return FA13_URL + "/transfer";
    }
}
