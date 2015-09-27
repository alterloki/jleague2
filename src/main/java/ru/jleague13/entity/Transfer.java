package ru.jleague13.entity;

import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 26.09.15 22:59.
 */
public class Transfer {

    private Date date;
    private List<Player> players;

    public Transfer() {
    }

    public Transfer(Date date, List<Player> players) {
        this.date = date;
        this.players = players;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
