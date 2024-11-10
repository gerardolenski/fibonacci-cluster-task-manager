package org.gol.taskmanager.task.application.service;

import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.JobRepositoryPort;
import org.gol.taskmanager.task.domain.JobsScheduler;
import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.task.Task;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Application service with task module use cases.
 */
@Slf4j
@Builder
class TaskManager implements TaskManagementPort {

    private final @NonNull JobsScheduler jobsScheduler;
    private final @NonNull JobRepositoryPort repositoryPort;

    @Override
    @Transactional
    public void scheduleProcessing(Task task) {
        jobsScheduler.scheduleProcessing(task);
    }

    @Override
    public void registerResult(JobResult result) {
        log.info("Registering result: {}", result);
        repositoryPort.persist(result);
    }

    @Override
    public TaskStats checkStatistics(TaskId taskId) {
        return repositoryPort.getStatistics(taskId);
    }
}
