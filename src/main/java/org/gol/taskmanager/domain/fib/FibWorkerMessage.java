package org.gol.taskmanager.domain.fib;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.gol.taskmanager.domain.model.WorkerMessage;
import org.gol.taskmanager.domain.model.WorkerType;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.gol.taskmanager.domain.model.WorkerType.FIBONACCI;

@Getter
@ToString
@RequiredArgsConstructor(access = PRIVATE)
class FibWorkerMessage implements WorkerMessage {

    private final String taskId;
    private final Integer number;
    private final String algorithm;

    static FibWorkerMessage ofFibWorkerMessage(UUID taskId, Integer number, FibAlgorithm algorithm) {
        return new FibWorkerMessage(taskId.toString(), number ,algorithm.name());
    }

    @Override
    public WorkerType getWorkerType() {
        return FIBONACCI;
    }
}
