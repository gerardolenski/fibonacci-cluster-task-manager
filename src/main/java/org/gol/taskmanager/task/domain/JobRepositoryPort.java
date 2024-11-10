package org.gol.taskmanager.task.domain;

import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;

public interface JobRepositoryPort {

    void persist(JobPersistCmd cmd);

    void persist(JobResult jobResult);

    TaskStats getStatistics(TaskId taskId);
}
