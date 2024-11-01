package org.gol.taskmanager.task.support;

import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobStats;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record TestJobStats(TaskId taskId,
                           TaskType taskType,
                           JobId jobId,
                           LocalDateTime startTime,
                           LocalDateTime stopTime,
                           String jobDetails,
                           String jobResult,
                           Long jobProcessingTime) implements JobStats {
}
