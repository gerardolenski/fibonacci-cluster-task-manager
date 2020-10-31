package org.gol.taskmanager.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskStatisticsData {
    UUID getTaskId();

    LocalDateTime getStartTime();

    LocalDateTime getStopTime();

    UUID getJobId();

    String getJobDetails();

    String getJobResult();

    Long getJobProcessingTime();
}
