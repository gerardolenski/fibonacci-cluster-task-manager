package org.gol.taskmanager.fib.application.adapter.rest;

import org.gol.taskmanager.fib.application.api.FibPort;
import org.gol.taskmanager.fib.domain.model.FibBase;
import org.gol.taskmanager.fib.domain.model.FibSeriesCalculationCmd;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.gol.taskmanager.fib.application.adapter.rest.FibTaskResponse.ofTaskId;
import static org.gol.taskmanager.fib.application.adapter.rest.ToResponseMapper.fromDomainResult;
import static org.springframework.http.HttpStatus.MULTI_STATUS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping("/fib")
@RequiredArgsConstructor
class FibRestAdapter {

    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final String TASK_ID = "{taskId:" + UUID_REGEX + "}";

    private final FibPort fibService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FibTaskResponse> processFibSeries(@RequestBody FibSeriesProcessingRequest body) {
        log.info("Received FIBONACCI calculation request: {}", body);
        var baseNums = body.series().stream()
                .map(FibBase::new)
                .toList();
        var taskId = fibService.calculateSeries(new FibSeriesCalculationCmd(baseNums));
        log.info("Successfully started FIBONACCI calculation task: taskId={}", taskId);
        return accepted().body(ofTaskId(taskId));
    }

    @GetMapping(value = TASK_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FibTaskResultResponse> getProcessingResult(@PathVariable("taskId") UUID taskId) {
        log.info("Received FIBONACCI processing result request: taskId={}", taskId);
        var result = fibService.checkResult(new FibTaskId(taskId));
        return ResponseEntity
                .status(MULTI_STATUS)
                .body(fromDomainResult(result));
    }
}
