package org.gol.taskmanager.domain.manager;

import org.gol.taskmanager.domain.model.WorkerData;

public interface WorkerManagerPort {

    void processTask(WorkerData task);
}
