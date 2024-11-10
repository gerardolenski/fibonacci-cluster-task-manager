package org.gol.taskmanager.task.domain;

import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.task.Task;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Domain service containing business logic of scheduling jobs;
 */
@Slf4j
@Builder
public class JobsScheduler {

    private final @NonNull JobNotificationPort notificationPort;
    private final @NonNull JobRepositoryPort repositoryPort;

    public void scheduleProcessing(Task task) {
        log.info("Scheduling task for processing: {}", task);
        var taskId = task.taskId();
        var taskType = task.type();

        task.jobs().collection()
                .forEach(job -> scheduleJob(taskId, taskType, job));
    }

    private void scheduleJob(TaskId taskId, TaskType taskType, Job job) {
        log.debug("Scheduling job: {}", job);
        repositoryPort.persist(new JobPersistCmd(taskId, taskType, job));
        notificationPort.notify(taskId, taskType, job);
    }
}
