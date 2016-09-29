package ru.jleague13.timer.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import ru.jleague13.timer.AbstractFaTask;
import ru.jleague13.timer.ProgressConnection;

/**
 * @author ashevenkov 24.09.16 20:31.
 */
@Component
public class SimpleLogTask extends AbstractFaTask {

    private Log log = LogFactory.getLog(SimpleLogTask.class);

    public SimpleLogTask() {
        super("simpleTask", "0 0 3 * * *");
    }

    @Override
    public void runTask(ProgressConnection progress) {
        int p = 0;
        log.info("Running simple task!!!");
        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                p += 10;
                progress.sendTaskProgress(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
