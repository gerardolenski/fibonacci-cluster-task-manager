package org.gol.taskmanager.fib.infrastructure.task;

import org.gol.taskmanager.fib.domain.FibTaskSchedulingPort;
import org.gol.taskmanager.fib.domain.model.FibClaim;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class FibTaskScheduler implements FibTaskSchedulingPort {

    private final TaskManagementPort taskManagementPort;

    @Override
    public FibTaskId scheduleFibonacciTask(List<FibClaim> claims) {
        log.debug("Scheduling fibonacci task: claims={}", claims);
        var task = FibTask.fromClaims(claims);
        taskManagementPort.scheduleProcessing(task);
        return new FibTaskId(task.taskId().value());
    }
}
