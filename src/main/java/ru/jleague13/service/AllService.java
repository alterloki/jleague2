package ru.jleague13.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import ru.jleague13.entity.AllZip;
import ru.jleague13.repository.AllDao;

/**
 * @author ashevenkov 23.04.16 13:47.
 */
@Component
public class AllService {





    public void reloadTodayAll() {

    }

    public AllZip loadTodayAll() {
        return new AllZip();
    }

    public void saveAll(AllZip allZip) {

    }
}
