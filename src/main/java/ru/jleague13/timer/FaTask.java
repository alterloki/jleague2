package ru.jleague13.timer;

/**
 * @author ashevenkov 24.09.16 18:02.
 */
public interface FaTask {

    void setTaskManagementSystem(TaskManagementSystem tms);

    String getName();

    String getCron();

    Runnable getToRun();

    void doOneRun();
}
