package org.gol.taskmanager.domain.fib;

import org.gol.taskmanager.domain.model.JobData;
import org.gol.taskmanager.domain.model.WorkerData;
import org.gol.taskmanager.domain.model.WorkerType;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;
import static org.gol.taskmanager.domain.model.WorkerType.FIBONACCI;

@Getter
@Accessors(fluent = true)
@ToString
@RequiredArgsConstructor(access = PRIVATE)
class FibWorkerData implements WorkerData {

    private final UUID taskId;
    private final JobData jobData;

    static FibWorkerData ofFibWorkerData(UUID taskId, Integer number, FibAlgorithm algorithm) {
        return new FibWorkerData(
                taskId,
                FibJobData.builder()
                        .jobId(randomUUID())
                        .number(number)
                        .algorithm(algorithm.name())
                        .jobDetails(format("FIBONACCI(%d) -> %s", number, algorithm.name()))
                        .build());
    }

    @Override
    public WorkerType workerType() {
        return FIBONACCI;
    }

    @Builder
    record FibJobData(UUID jobId, Integer number, String algorithm, String jobDetails) implements JobData {
    }
}
