package ru.jleague13.timer;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author ashevenkov 24.09.16 20:02.
 */
public class ProgressConnection {

    private String taskName;
    private TaskManagementSystem tms;

    public ProgressConnection(String taskName, TaskManagementSystem tms) {
        this.taskName = taskName;
        this.tms = tms;
    }

    public void sendTaskProgress(int progress) {
        CompletableFuture.runAsync(() -> tms.sendProgress(taskName, progress));
    }
}
