package ru.jleague13.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ashevenkov 24.09.16 19:00.
 */
public abstract class AbstractFaTask implements FaTask {

    private Log log = LogFactory.getLog(AbstractFaTask.class);

    private String name;
    private String cron;
    private Runnable runnable;
    private Lock lock;
    private TaskManagementSystem tms;
    private ProgressConnection progress;

    public AbstractFaTask(String name, String cron) {
        this.name = name;
        this.cron = cron;
        this.lock = new ReentrantLock();
        this.runnable = this::doOneRun;
    }

    public void doOneRun() {
        lock.lock();
        tms.logStatus(this.name, TaskStatus.RUNNING, "Running task " + this.name);
        tms.sendProgress(this.name, 0);
        try {
            runTask(progress);
            tms.sendProgress(this.name, 100);
            tms.logStatus(this.name, TaskStatus.FINISHED, "Finished task " + this.name);
        } catch (Exception e) {
            log.error(e);
            tms.logStatus(this.name, TaskStatus.ERROR, e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Autowired
    public void setTaskManagementSystem(TaskManagementSystem tms) {
        this.tms = tms;
        this.progress = new ProgressConnection(this.name, tms);
    }

    public abstract void runTask(ProgressConnection progress) throws Exception;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCron() {
        return this.cron;
    }

    @Override
    public Runnable getToRun() {
        return this.runnable;
    }
}
