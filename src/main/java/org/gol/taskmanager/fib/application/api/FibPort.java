package org.gol.taskmanager.fib.application.api;

import org.gol.taskmanager.fib.domain.model.FibSeriesCalculationCmd;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.fib.domain.model.result.FibTaskResult;

/**
 * Primary port to implement fib module use cases.
 */
public interface FibPort {

    FibTaskId calculateSeries(FibSeriesCalculationCmd cmd);

    FibTaskResult checkResult(FibTaskId taskId);
}
