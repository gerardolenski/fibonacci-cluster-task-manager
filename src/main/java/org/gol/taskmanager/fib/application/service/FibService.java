package org.gol.taskmanager.fib.application.service;

import org.gol.taskmanager.fib.application.api.FibPort;
import org.gol.taskmanager.fib.domain.FibCalculatorFactory;
import org.gol.taskmanager.fib.domain.FibTaskResultPort;
import org.gol.taskmanager.fib.domain.model.FibSeriesCalculationCmd;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.fib.domain.model.result.FibTaskResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Application service with fib module use cases.
 */
@Service
@RequiredArgsConstructor
class FibService implements FibPort {

    private final FibCalculatorFactory fibCalculatorFactory;
    private final FibTaskResultPort taskResultPort;

    @Override
    public FibTaskId calculateSeries(FibSeriesCalculationCmd cmd) {
        return fibCalculatorFactory.create(cmd.baseNums())
                .calculateSeries()
                .calculationTaskId()
                .orElseThrow();
    }

    @Override
    public FibTaskResult checkResult(FibTaskId taskId) {
        return taskResultPort.checkResult(taskId);
    }
}
