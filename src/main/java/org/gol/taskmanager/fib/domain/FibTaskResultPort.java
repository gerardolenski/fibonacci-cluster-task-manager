package org.gol.taskmanager.fib.domain;

import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.fib.domain.model.result.FibTaskResult;

public interface FibTaskResultPort {

    FibTaskResult checkResult(FibTaskId taskId);
}
