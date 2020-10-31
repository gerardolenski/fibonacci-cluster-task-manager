package org.gol.taskmanager.application.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
class TaskAdapter {

    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final String TASK_ID = "{taskId:" + UUID_REGEX + "}";

    private final WorkerRepositoryPort workerRepositoryPort;

    @GetMapping(value = TASK_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResponse> getFibResults(@PathVariable("taskId") UUID taskId) {
        var jobs = workerRepositoryPort.getStatistics(taskId);
        return ok().body(TaskResponse.builder()
                .taskId(taskId)
                .jobsCount(jobs.size())
                .jobs(jobs)
                .build());
    }
}
