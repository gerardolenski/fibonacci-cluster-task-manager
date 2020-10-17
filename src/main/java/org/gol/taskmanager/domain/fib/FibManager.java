package org.gol.taskmanager.domain.fib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerManagerPort;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.gol.taskmanager.domain.fib.FibAlgorithm.*;
import static org.gol.taskmanager.domain.fib.FibWorkerMessage.ofFibWorkerMessage;

@Slf4j
@RequiredArgsConstructor
class FibManager implements FibPort {

    private final WorkerManagerPort workerManagerPort;

    private static final Predicate<Integer> NATURAL_NUMBER = i -> i >= 0;

    @Override
    public UUID calculateSeries(List<Integer> series) {
        var taskId = UUID.randomUUID();
        var inList = series.stream()
                .filter(Objects::nonNull)
                .filter(NATURAL_NUMBER)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        log.info("Start task {} for calculate FIBONACCI series: {}", taskId, inList);

        inList.stream()
                .flatMap(number -> Stream.of(
                        ofFibWorkerMessage(taskId, number, RECURSIVE),
                        ofFibWorkerMessage(taskId, number, ITERATIVE),
                        ofFibWorkerMessage(taskId, number, BINETS)
                ))
                .forEach(workerManagerPort::processTask);
        return taskId;
    }
}
