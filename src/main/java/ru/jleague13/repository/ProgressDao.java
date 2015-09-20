package ru.jleague13.repository;

import ru.jleague13.entity.ProgressType;

/**
 * @author ashevenkov 19.09.15 18:56.
 */
public interface ProgressDao {

    void startProgress(ProgressType progressType);

    boolean getProgress(ProgressType progressType);

    void finishProgress(ProgressType progressType);
}
