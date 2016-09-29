package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jleague13.timer.TaskInfo;
import ru.jleague13.timer.TaskLogRecord;
import ru.jleague13.timer.TaskStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 24.09.16 21:20.
 */
@Repository
public class DbTaskDao implements TaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String TASK_LOG_FULL_FIELDS = "tl.task_name, tl.log_time, tl.status, tl.message";
    private static String TASK_INFO_FULL_FIELDS = "ti.name, ti.cron, ti.progress";


    @Override
    public void addStatus(String taskName, TaskStatus status, String message, Date date) {
        jdbcTemplate.update("insert into task_log (task_name, log_time, status, message) values (?,?,?,?)",
                taskName, date, status.ordinal(), message);
    }

    @Override
    public List<TaskLogRecord> getAllLastLogs(int count) {
        return jdbcTemplate.query(
                "select " + TASK_LOG_FULL_FIELDS +
                        " from (select * from task_log tl order by id desc) tl limit ?",
                (resultSet, i) -> taskLogFromRs(resultSet), count);
    }

    private TaskLogRecord taskLogFromRs(ResultSet rs) throws SQLException {
        return new TaskLogRecord(rs.getString("task_name"), rs.getTimestamp("log_time"),
                TaskStatus.values()[rs.getInt("status")], rs.getString("message"));
    }

    @Override
    public List<TaskLogRecord> getTaskLastLogs(String taskName, int count) {
        return jdbcTemplate.query(
                "select " + TASK_LOG_FULL_FIELDS +
                        " from (select * from task_log tl where name = ? order by id desc) tl limit ?",
                (resultSet, i) -> taskLogFromRs(resultSet), taskName, count);
    }

    @Override
    public void registerTask(String name, String cron) {
        jdbcTemplate.update("insert into task_info (name, cron, progress) values (?,?,?)",
                name, cron, 0);
    }

    @Override
    public void deleteAllTasks() {
        jdbcTemplate.update("delete from task_info");
    }

    @Override
    public List<TaskInfo> getTaskInfoList() {
        return jdbcTemplate.query(
                "select " + TASK_INFO_FULL_FIELDS + ", " + TASK_LOG_FULL_FIELDS +
                        " from task_info ti left join " +
                        "(" +
                        "  SELECT" +
                        "    max(id) tid, " +
                        "    task_name " +
                        "  FROM task_log\n" +
                        "  GROUP BY task_name\n" +
                        ") v1 on ti.name = v1.task_name\n" +
                        "  LEFT JOIN task_log tl on v1.tid = tl.id\n" +
                        "order by ti.name;\n",
                (resultSet, i) -> taskInfoFromRs(resultSet));
    }

    @Override
    public void updateProgress(String taskName, int progress) {
        jdbcTemplate.update("update task_info set progress=? where name = ?",
                progress, taskName);
    }

    private TaskInfo taskInfoFromRs(ResultSet rs) throws SQLException {
        return new TaskInfo(rs.getString("name"),
                rs.getString("cron"), taskLogFromRs(rs), rs.getInt("progress"));
    }
}
