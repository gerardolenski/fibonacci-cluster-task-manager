package org.gol.taskmanager.fib.domain.model.result;

import org.gol.taskmanager.fib.domain.model.FibNumber;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FibSuccessResult(
        @NonNull FibJobId jobId,
        @NonNull LocalDateTime startTime,
        @NonNull LocalDateTime stopTime,
        @NonNull String jobDescription,
        @NonNull FibNumber number,
        @NonNull Long jobProcessingTime) {
}
