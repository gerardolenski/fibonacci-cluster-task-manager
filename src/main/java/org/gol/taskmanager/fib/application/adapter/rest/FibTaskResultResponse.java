package org.gol.taskmanager.fib.application.adapter.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;
import java.util.List;

import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
record FibTaskResultResponse(
        String taskId,
        long requestedNumbersCount,
        long finishedWithSuccessCount,
        long finishedWithFailureCount,
        long inProgressCount,
        List<JobResponse> results) {

    @Builder
    @JsonInclude(NON_NULL)
    record JobResponse(int statusCode,
                       String jobDescription,
                       BigInteger fibonacciNumber,
                       String startTime,
                       String stopTime,
                       Long jobProcessingTime,
                       String errorMessage) {
    }
}