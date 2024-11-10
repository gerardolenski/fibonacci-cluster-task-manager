package org.gol.taskmanager.fib.domain.model;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public record FibSeriesCalculationCmd(List<FibBase> baseNums) {
    public FibSeriesCalculationCmd {
        if (isEmpty(baseNums))
            throw new IllegalArgumentException("Fibonacci base number collection cannot be empty.");
    }
}
