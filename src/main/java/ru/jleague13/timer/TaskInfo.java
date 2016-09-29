package ru.jleague13.timer;

import java.util.Date;

/**
 * @author ashevenkov 24.09.16 20:53.
 */
public class TaskInfo {

    private String taskName;
    private String cron;
    private TaskLogRecord lastRecord;
    private int progress;

    public TaskInfo(String taskName, String cron, TaskLogRecord lastRecord, int progress) {
        this.taskName = taskName;
        this.cron = cron;
        this.lastRecord = lastRecord;
        this.progress = progress;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getCron() {
        return cron;
    }

    public TaskLogRecord getLastRecord() {
        return lastRecord;
    }

    public int getProgress() {
        return progress;
    }
}
