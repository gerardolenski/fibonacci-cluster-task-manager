package org.gol.taskmanager.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskStatisticsData {
    UUID taskId();

    LocalDateTime startTime();

    LocalDateTime stopTime();

    UUID jobId();

    String jobDetails();

    String jobResult();

    Long jobProcessingTime();
}
