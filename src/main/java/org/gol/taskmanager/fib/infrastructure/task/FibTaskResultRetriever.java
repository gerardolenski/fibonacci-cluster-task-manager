package org.gol.taskmanager.fib.infrastructure.task;

import org.gol.taskmanager.fib.domain.FibTaskResultPort;
import org.gol.taskmanager.fib.domain.model.FibNumber;
import org.gol.taskmanager.fib.domain.model.FibTaskId;
import org.gol.taskmanager.fib.domain.model.result.FibFailureResult;
import org.gol.taskmanager.fib.domain.model.result.FibJobId;
import org.gol.taskmanager.fib.domain.model.result.FibSuccessResult;
import org.gol.taskmanager.fib.domain.model.result.FibTaskResult;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.model.job.JobStats;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import static java.util.Collections.unmodifiableList;

@Service
@RequiredArgsConstructor
class FibTaskResultRetriever implements FibTaskResultPort {

    private final TaskManagementPort taskManagementPort;

    @Override
    public FibTaskResult checkResult(FibTaskId taskId) {
        var stats = taskManagementPort.checkStatistics(new TaskId(taskId.value()));

        var jobsAccumulator = stats.finishedJobs().stream()
                .map(this::toResult)
                .reduce(JobsAccumulator.create(), JobsAccumulator::add, (a1, a2) -> a1);
        return FibTaskResult.builder()
                .taskId(taskId)
                .scheduledJobsCount(stats.scheduledJobsCount())
                .successes(unmodifiableList(jobsAccumulator.successes()))
                .failures(unmodifiableList(jobsAccumulator.failures()))
                .build();
    }

    Either<FibFailureResult, FibSuccessResult> toResult(JobStats jobStats) {
        return Try.of(() -> new BigInteger(jobStats.jobResult()))
                .map(n -> FibSuccessResult.builder()
                        .jobId(new FibJobId(jobStats.jobId().value()))
                        .jobDescription(jobStats.jobDetails())
                        .jobProcessingTime(jobStats.jobProcessingTime())
                        .startTime(jobStats.startTime())
                        .stopTime(jobStats.stopTime())
                        .number(new FibNumber(n))
                        .build())
                .toEither(() -> FibFailureResult.builder()
                        .jobId(new FibJobId(jobStats.jobId().value()))
                        .jobDescription(jobStats.jobDetails())
                        .errorMessage(jobStats.jobResult())
                        .jobProcessingTime(jobStats.jobProcessingTime())
                        .startTime(jobStats.startTime())
                        .stopTime(jobStats.stopTime())
                        .build());
    }

    record JobsAccumulator(List<FibSuccessResult> successes, List<FibFailureResult> failures) {

        public static JobsAccumulator create() {
            return new JobsAccumulator(new LinkedList<>(), new LinkedList<>());
        }

        JobsAccumulator add(Either<FibFailureResult, FibSuccessResult> result) {
            if (result.isRight())
                successes.add(result.get());
            else
                failures.add(result.getLeft());
            return this;
        }
    }
}
