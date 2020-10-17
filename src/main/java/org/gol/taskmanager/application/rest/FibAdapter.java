package org.gol.taskmanager.application.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.fib.FibPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.gol.taskmanager.application.rest.FibResponse.ofFibResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping("/fib")
@RequiredArgsConstructor
class FibAdapter {

    private final FibPort fibManager;

    @PostMapping(value = "calculate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<FibResponse> processFibSeries(@RequestBody FibRequest body) {
        log.info("Received FIBONACCI calculation request: {}", body);
        var taskId = fibManager.calculateSeries(body.getSeries());
        log.info("Successfully start all FIBONACCI calculation tasks with id: {}", taskId);
        return accepted().body(ofFibResponse(taskId));
    }
}
