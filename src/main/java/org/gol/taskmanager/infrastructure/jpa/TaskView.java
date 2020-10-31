package org.gol.taskmanager.infrastructure.jpa;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gol.taskmanager.domain.model.TaskStatisticsData;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class TaskView implements TaskStatisticsData {
    private final UUID taskId;
    private final LocalDateTime startTime;
    private final LocalDateTime stopTime;
    private final UUID jobId;
    private final String jobDetails;
    private final String jobResult;
    private final Long jobProcessingTime;
}
