package org.gol.taskmanager.task.support;

import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.task.TaskId;

import lombok.Builder;

@Builder
public record TestJobResult(
        TaskId taskId,
        JobId jobId,
        String result,
        Long processingTime,
        String errorMessage) implements JobResult {
}
