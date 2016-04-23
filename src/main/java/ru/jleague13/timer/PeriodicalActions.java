package ru.jleague13.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.jleague13.controller.InformationManager;
import ru.jleague13.service.AllService;

/**
 * @author ashevenkov 23.04.16 13:44.
 */
@Component
public class PeriodicalActions {

    @Autowired
    private InformationManager informationManager;


    @Scheduled(cron="0 0 6 * * ?")
    public void loadAllZip() {
        informationManager.updateTodayAll();
    }
}
