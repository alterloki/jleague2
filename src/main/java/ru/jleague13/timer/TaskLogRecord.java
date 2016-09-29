package ru.jleague13.timer;

import java.util.Date;

/**
 * @author ashevenkov 24.09.16 20:35.
 */
public class TaskLogRecord {

    private String taskName;
    private Date time;
    private TaskStatus status;
    private String message;

    public TaskLogRecord(String taskName, Date time, TaskStatus status, String message) {
        this.taskName = taskName;
        this.time = time;
        this.status = status;
        this.message = message;
    }

    public String getTaskName() {
        return taskName;
    }

    public Date getTime() {
        return time;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
