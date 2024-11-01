package org.gol.taskmanager.task.domain.model.job;

import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

import java.time.LocalDateTime;

public interface JobStats {

    TaskId taskId();

    TaskType taskType();

    JobId jobId();

    LocalDateTime startTime();

    LocalDateTime stopTime();

    String jobDetails();

    String jobResult();

    Long jobProcessingTime();
}
