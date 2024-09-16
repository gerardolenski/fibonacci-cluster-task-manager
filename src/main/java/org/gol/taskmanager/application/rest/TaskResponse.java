package org.gol.taskmanager.application.rest;

import org.gol.taskmanager.domain.model.TaskStatisticsData;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
record TaskResponse(UUID taskId, int jobsCount, List<TaskStatisticsData> jobs) {
}
