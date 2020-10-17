package org.gol.taskmanager.domain.manager;

import org.gol.taskmanager.domain.model.WorkerMessage;

public interface WorkerManagerPort {

    void processTask(WorkerMessage task);
}
