package org.gol.taskmanager.fib.domain.model.result;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FibFailureResult(
        @NonNull FibJobId jobId,
        @NonNull LocalDateTime startTime,
        @NonNull LocalDateTime stopTime,
        @NonNull String jobDescription,
        @NonNull String errorMessage,
        @NonNull Long jobProcessingTime) {
}
