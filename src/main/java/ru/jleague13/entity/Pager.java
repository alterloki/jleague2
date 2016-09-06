package ru.jleague13.entity;

import java.util.List;

/**
 * @author ashevenkov 06.09.16 17:26.
 */
public class Pager {

    public String active;
    public List<String> indexes;

    public Pager(String active, List<String> indexes) {
        this.active = active;
        this.indexes = indexes;
    }
}
