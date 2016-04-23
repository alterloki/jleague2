package ru.jleague13.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.jleague13.controller.InformationManager;

import java.io.IOException;

/**
 * @author ashevenkov 23.04.16 13:44.
 */
@Component
public class PeriodicalActions {

    private Log log = LogFactory.getLog(PeriodicalActions.class);

    @Autowired
    private InformationManager informationManager;

    @Scheduled(cron="0 0 6 * * ?")
    public void loadAllZip() {
        try {
            informationManager.updateTodayAll();
        } catch (IOException e) {
            log.error("Problem updating all13.zip", e);
        }
    }
}
