package org.gol.taskmanager.task.application.adapter.jms;

import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.task.TaskId;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import static java.util.Optional.ofNullable;

@Getter
@ToString
@Accessors(fluent = true)
@RequiredArgsConstructor
class JobResultMessage implements JobResult {

    private final UUID taskId;
    private final UUID jobId;
    private final Object result;
    private final Long processingTime;
    private final String errorMessage;

    @Override
    public TaskId taskId() {
        return ofNullable(taskId)
                .map(TaskId::new)
                .orElse(null);
    }

    @Override
    public JobId jobId() {
        return ofNullable(jobId)
                .map(JobId::new)
                .orElse(null);
    }

    @Override
    public String result() {
        return ofNullable(result)
                .map(Object::toString)
                .orElse(null);
    }
}
