package org.gol.taskmanager.fib.domain.model.result;

import org.gol.taskmanager.fib.domain.model.FibTaskId;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FibTaskResult(
        @NonNull FibTaskId taskId,
        long scheduledJobsCount,
        @NonNull List<FibSuccessResult> successes,
        @NonNull List<FibFailureResult> failures) {

    public long finishedJobsCount() {
        return successes.size() + failures.size();
    }

    public long successesCount() {
        return successes.size();
    }

    public long failuresCount() {
        return failures.size();
    }
}

