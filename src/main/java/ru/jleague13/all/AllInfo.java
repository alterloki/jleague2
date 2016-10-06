package ru.jleague13.all;

import java.util.Date;

/**
 * @author ashevenkov 05.10.16 16:37.
 */
public class AllInfo {

    private int id;
    private Date allDate;
    private boolean parsed;

    public AllInfo(int id, Date allDate, boolean parsed) {
        this.id = id;
        this.allDate = allDate;
        this.parsed = parsed;
    }

    public int getId() {
        return id;
    }

    public Date getAllDate() {
        return allDate;
    }

    public boolean isParsed() {
        return parsed;
    }
}
