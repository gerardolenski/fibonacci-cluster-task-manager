package org.gol.taskmanager.domain.manager;

import org.gol.taskmanager.domain.model.ResultData;
import org.gol.taskmanager.domain.model.TaskStatisticsData;
import org.gol.taskmanager.domain.model.WorkerData;

import java.util.List;
import java.util.UUID;

public interface WorkerRepositoryPort {
    void persist(WorkerData workerData);

    void persist(ResultData resultData);

    List<TaskStatisticsData> getStatistics(UUID taskId);
}
