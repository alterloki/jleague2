package ru.jleague13.timer.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.calendar.Event;
import ru.jleague13.calendar.EventType;
import ru.jleague13.download.FaUrlResolver;
import ru.jleague13.entity.Player;
import ru.jleague13.entity.Transfer;
import ru.jleague13.repository.CalendarEventsDao;
import ru.jleague13.repository.TransferDao;
import ru.jleague13.timer.AbstractFaTask;
import ru.jleague13.timer.ProgressConnection;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ashevenkov 14.01.17 21:19.
 */
@Component
public class TransferTask extends AbstractFaTask {

    private Log log = LogFactory.getLog(TransferTask.class);

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private FaUrlResolver faUrlResolver;

    @Autowired
    private CalendarEventsDao calendarEventsDao;

    public TransferTask() {
        super("transferTask", "0 0 4 * * *");
    }

    @Override
    public void runTask(ProgressConnection progress) throws Exception {
        List<Event> dayEvents = calendarEventsDao.getDayEvents(new Date());
        for (Event dayEvent : dayEvents) {
            if(dayEvent.getEventType() == EventType.TRANSFER_FINAL) {
                log.info("Today final transfer list! Loading new transfer list.");
                URL url = new URL(faUrlResolver.getTransferListUrl());
                Transfer transfer = transferDao.readTransfer(
                        new InputStreamReader(url.openStream(), "cp1251"));
                transferDao.saveTransfer(transfer);
            } else if(dayEvent.getEventType() == EventType.T_AUCTION) {
                log.info("Today is auction! Loading transfer results.");
                URL url = new URL(faUrlResolver.getTransferResultUrl());
                Date lastDate = transferDao.allTransferDates().get(0);
                Map<String, Player> resultMap = transferDao.readTransferResult(
                        new InputStreamReader(url.openStream(), "utf-8"), lastDate);
                if(transferDao.haveTransfer(lastDate)) {
                    Transfer transfer = transferDao.loadTransfer(lastDate);
                    List<Player> players = transfer.getPlayers();
                    for (Player player : players) {
                        Player withPrice = resultMap.get(player.getName());
                        if(withPrice != null) {
                            player.setPayed(withPrice.getPayed());
                            player.setBuyer(withPrice.getBuyer());
                            transferDao.saveTransferResult(player);
                        }
                    }
                }
            }
        }
    }
}
