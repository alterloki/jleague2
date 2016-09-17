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

    public String indexLink(String index) {
        int skip = 0;
        int cur = Integer.parseInt(active);
        switch (index) {
            case "<":
                cur--;
                break;
            case ">":
                cur++;
                break;
            default:
                cur = Integer.parseInt(index);
                break;
        }
        skip = (cur - 1) * 10;
        String countPart = "";
        if(skip > 0) {
            countPart = "?skip=" + skip;
        }
        return "/new/admin/images" + countPart;
    }
}
