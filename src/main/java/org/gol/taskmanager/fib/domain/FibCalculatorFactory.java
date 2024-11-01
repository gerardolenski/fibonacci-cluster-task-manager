package org.gol.taskmanager.fib.domain;


import org.gol.taskmanager.fib.domain.model.FibAlgorithm;
import org.gol.taskmanager.fib.domain.model.FibBase;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FibCalculatorFactory {

    private final Set<FibAlgorithm> fibAlgorithms;
    private final FibTaskSchedulingPort fibTaskSchedulingPort;

    public FibCalculator create(List<FibBase> baseNums) {
        return new FibCalculator(baseNums, fibAlgorithms, fibTaskSchedulingPort);
    }
}
