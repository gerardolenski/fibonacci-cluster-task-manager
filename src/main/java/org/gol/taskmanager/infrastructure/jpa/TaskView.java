package org.gol.taskmanager.infrastructure.jpa;

import org.gol.taskmanager.domain.model.TaskStatisticsData;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
record TaskView(
        UUID taskId,
        LocalDateTime startTime,
        LocalDateTime stopTime,
        UUID jobId,
        String jobDetails,
        String jobResult,
        Long jobProcessingTime) implements TaskStatisticsData {
}
