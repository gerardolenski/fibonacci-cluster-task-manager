package org.gol.taskmanager.domain.fib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerManagerPort;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.gol.taskmanager.domain.fib.FibWorkerData.ofFibWorkerData;

@Slf4j
@RequiredArgsConstructor
class FibManager implements FibPort {

    private static final Predicate<Integer> NATURAL_NUMBER = i -> i >= 0;

    private final WorkerManagerPort workerManagerPort;
    private final Set<FibAlgorithm> activeAlgorithms;

    @Override
    public UUID calculateSeries(List<Integer> series) {
        var taskId = UUID.randomUUID();
        var inList = series.stream()
                .filter(Objects::nonNull)
                .filter(NATURAL_NUMBER)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        log.info("Starting task {} of FIBONACCI series calculation for: {}", taskId, inList);

        inList.stream()
                .flatMap(number -> activeAlgorithms.stream()
                        .map(algorithm -> ofFibWorkerData(taskId, number, algorithm)))
                .forEach(workerManagerPort::processTask);

        return taskId;
    }
}
