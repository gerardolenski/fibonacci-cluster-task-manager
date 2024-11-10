package org.gol.taskmanager.fib.application.adapter.rest;

import org.gol.taskmanager.fib.domain.model.result.FibFailureResult;
import org.gol.taskmanager.fib.domain.model.result.FibSuccessResult;
import org.gol.taskmanager.fib.domain.model.result.FibTaskResult;

import java.util.stream.Stream;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;

@NoArgsConstructor(access = PRIVATE)
class ToResponseMapper {

    static FibTaskResultResponse fromDomainResult(FibTaskResult result) {
        return FibTaskResultResponse.builder()
                .taskId(result.taskId().value().toString())
                .requestedNumbersCount(result.scheduledJobsCount())
                .finishedWithSuccessCount(result.successesCount())
                .finishedWithFailureCount(result.failuresCount())
                .inProgressCount(result.scheduledJobsCount() - result.finishedJobsCount())
                .results(Stream.of(result.successes().stream().map(ToResponseMapper::toJob), result.failures().stream().map(ToResponseMapper::toJob))
                        .flatMap(s -> s)
                        .toList())
                .build();
    }

    private static FibTaskResultResponse.JobResponse toJob(FibSuccessResult result) {
        return FibTaskResultResponse.JobResponse.builder()
                .statusCode(OK.value())
                .jobDescription(result.jobDescription())
                .fibonacciNumber(result.number().value())
                .startTime(result.startTime().toString())
                .stopTime(result.stopTime().toString())
                .jobProcessingTime(result.jobProcessingTime())
                .build();
    }

    private static FibTaskResultResponse.JobResponse toJob(FibFailureResult result) {
        return FibTaskResultResponse.JobResponse.builder()
                .statusCode(NOT_ACCEPTABLE.value())
                .jobDescription(result.jobDescription())
                .errorMessage(result.errorMessage())
                .startTime(result.startTime().toString())
                .stopTime(result.stopTime().toString())
                .jobProcessingTime(result.jobProcessingTime())
                .build();
    }
}
