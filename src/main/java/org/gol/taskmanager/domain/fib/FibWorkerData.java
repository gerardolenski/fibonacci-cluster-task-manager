package org.gol.taskmanager.domain.fib;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.gol.taskmanager.domain.model.JobData;
import org.gol.taskmanager.domain.model.WorkerData;
import org.gol.taskmanager.domain.model.WorkerType;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;
import static org.gol.taskmanager.domain.model.WorkerType.FIBONACCI;

@Getter
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
                        .build());
    }

    @Override
    public WorkerType getWorkerType() {
        return FIBONACCI;
    }

    @Getter
    @Builder
    @ToString
    static class FibJobData implements JobData {
        private final UUID jobId;
        private final Integer number;
        private final String algorithm;
    }
}
