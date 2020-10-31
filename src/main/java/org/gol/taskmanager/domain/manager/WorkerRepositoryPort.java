package org.gol.taskmanager.domain.manager;

import org.gol.taskmanager.domain.model.ResultData;
import org.gol.taskmanager.domain.model.WorkerData;

public interface WorkerRepositoryPort {
    void persist(WorkerData workerData);
    void persist(ResultData resultData);
}
