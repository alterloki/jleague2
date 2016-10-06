package ru.jleague13.timer.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.all.AllManager;
import ru.jleague13.download.DownloadInfo;
import ru.jleague13.timer.AbstractFaTask;
import ru.jleague13.timer.ProgressConnection;

/**
 * @author ashevenkov 29.09.16 11:32.
 */
@Component
public class DownloadAllTask extends AbstractFaTask {

    private Log log = LogFactory.getLog(DownloadAllTask.class);

    @Autowired
    private AllManager allManager;

    public DownloadAllTask() {
        super("downloadAllTask", "0 0 4 * * *");
    }

    @Override
    public void runTask(ProgressConnection progress) throws Exception {
        log.info("Running downolad all.zip task");
        allManager.downloadCurrentAllFile();
    }

}
