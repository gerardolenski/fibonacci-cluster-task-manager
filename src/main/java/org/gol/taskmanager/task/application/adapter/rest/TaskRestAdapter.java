package org.gol.taskmanager.task.application.adapter.rest;

import org.gol.taskmanager.task.application.adapter.rest.TaskResponse.JobResponse;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
class TaskRestAdapter {

    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final String TASK_ID = "{taskId:" + UUID_REGEX + "}";

    private final TaskManagementPort taskManagementPort;

    @GetMapping(value = TASK_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResponse> getTaskResults(@PathVariable("taskId") UUID taskId) {
        var stats = taskManagementPort.checkStatistics(new TaskId(taskId));
        return ok().body(TaskResponse.builder()
                .taskId(taskId.toString())
                .scheduledJobsCount(stats.scheduledJobsCount())
                .finishedJobsCount(stats.finishedJobsCount())
                .finishedJobs(stats.finishedJobs().stream().map(JobResponse::fromStats).toList())
                .build());
    }
}
