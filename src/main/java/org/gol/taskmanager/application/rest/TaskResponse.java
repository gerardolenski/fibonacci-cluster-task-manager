package org.gol.taskmanager.application.rest;

import lombok.Builder;
import lombok.Getter;
import org.gol.taskmanager.domain.model.TaskStatisticsData;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
class TaskResponse {
    private final UUID taskId;
    private final int jobsCount;
    private final List<TaskStatisticsData> jobs;
}
