package org.gol.taskmanager.task.domain.model.job;

import org.gol.taskmanager.task.domain.model.task.TaskId;

public interface JobResult {

    TaskId taskId();

    JobId jobId();

    String result();

    Long processingTime();

    String errorMessage();
}
