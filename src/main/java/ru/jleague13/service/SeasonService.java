package ru.jleague13.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * //TODO move to DB
 * @author ashevenkov 05.02.17 23:05.
 */
@Service
public class SeasonService {

    public Date getSeasonStart() {
        Date result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            result = dateFormat.parse("01.09.2017");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Date getSeasonFinish() {
        Date result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            result = dateFormat.parse("05.01.2018");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
