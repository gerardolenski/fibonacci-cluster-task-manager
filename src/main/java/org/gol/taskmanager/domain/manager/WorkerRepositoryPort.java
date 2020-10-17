package org.gol.taskmanager.domain.manager;

import org.gol.taskmanager.domain.model.WorkerData;

public interface WorkerRepositoryPort {
    void persist(WorkerData workerData);
}
