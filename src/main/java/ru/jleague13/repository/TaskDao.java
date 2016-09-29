package ru.jleague13.repository;

import ru.jleague13.timer.TaskInfo;
import ru.jleague13.timer.TaskLogRecord;
import ru.jleague13.timer.TaskStatus;

import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 24.09.16 20:34.
 */
public interface TaskDao {

    void addStatus(String taskName, TaskStatus status, String message, Date date);

    List<TaskLogRecord> getAllLastLogs(int count);

    List<TaskLogRecord> getTaskLastLogs(String taskName, int count);

    void registerTask(String name, String cron);

    void deleteAllTasks();

    List<TaskInfo> getTaskInfoList();

    void updateProgress(String taskName, int progress);
}
