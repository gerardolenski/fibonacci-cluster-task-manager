package org.gol.taskmanager.fib.domain;

import org.gol.taskmanager.fib.domain.model.FibClaim;
import org.gol.taskmanager.fib.domain.model.FibTaskId;

import java.util.List;

public interface FibTaskSchedulingPort {

    FibTaskId scheduleFibonacciTask(List<FibClaim> claims);
}
