package org.gol.taskmanager.fib.domain;

import org.gol.taskmanager.fib.domain.model.FibAlgorithm;
import org.gol.taskmanager.fib.domain.model.FibBase;
import org.gol.taskmanager.fib.domain.model.FibClaim;
import org.gol.taskmanager.fib.domain.model.FibTaskId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Comparator.comparingInt;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@RequiredArgsConstructor(access = PACKAGE)
public class FibCalculator {

    private final List<FibBase> baseNums;
    private final Set<FibAlgorithm> activeAlgorithms;
    private final FibTaskSchedulingPort taskSchedulingPort;

    private FibTaskId calculationTaskId;

    public FibCalculator calculateSeries() {
        if (Objects.nonNull(calculationTaskId))
            throw new IllegalStateException("Fibonacci series were already calculated");
        var claims = baseNums.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted(comparingInt(FibBase::n))
                .flatMap(base -> activeAlgorithms.stream()
                        .map(algorithm -> new FibClaim(base, algorithm)))
                .toList();
        this.calculationTaskId = taskSchedulingPort.scheduleFibonacciTask(claims);
        return this;
    }

    public Optional<FibTaskId> calculationTaskId() {
        return ofNullable(calculationTaskId);
    }
}
