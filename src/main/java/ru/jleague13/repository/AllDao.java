package ru.jleague13.repository;

import ru.jleague13.all.AllInfo;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ashevenkov 23.04.16 14:13.
 */
public interface AllDao {

    boolean haveAllOnDate(Date date);

    int registerAll(Date date);

    void makeParsed(Date date);

    List<AllInfo> getRegistered();

    void deleteAllBefore(Date date);

    AllInfo getAllInfoOnDate(Date date);

    AllInfo getById(int allId);

    AllInfo getLastAll();
}
