package ru.jleague13.prediction;

import ru.jleague13.entity.Match;

import java.util.List;

/**
 * @author ashevenkov 01.02.17 20:17.
 */
public class MatchesList {

    private List<Match> matches;

    public MatchesList() {
    }

    public MatchesList(List<Match> matches) {
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

}
