package org.gol.taskmanager.fib.infrastructure.task;

import org.gol.taskmanager.fib.domain.model.FibAlgorithm;
import org.gol.taskmanager.fib.domain.model.FibBase;
import org.gol.taskmanager.fib.domain.model.FibClaim;
import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.job.JobDetails;
import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.Jobs;
import org.gol.taskmanager.task.domain.model.task.Task;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.gol.taskmanager.task.domain.model.task.TaskType.FIBONACCI;

@Getter
@Accessors(fluent = true)
@ToString
@RequiredArgsConstructor(access = PRIVATE)
class FibTask implements Task {

    private final TaskId taskId;
    private final Jobs jobs;

    static FibTask fromClaims(List<FibClaim> claims) {
        return new FibTask(
                TaskId.generateTaskId(),
                new Jobs(claims.stream().map(FibTask::fromClaim).toList()));
    }

    private static Job fromClaim(FibClaim claim) {
        var details = new FibJobDetails(claim.base(), claim.algorithm());
        return new FibJob(JobId.generateJobId(), details);
    }

    @Override
    public TaskType type() {
        return FIBONACCI;
    }

    @Builder
    record FibJob(JobId jobId, FibJobDetails jobDetails) implements Job {
    }

    record FibJobDetails(FibBase base, FibAlgorithm algorithm) implements JobDetails {

        @Override
        public String serializedFormat() {
            return format("FIBONACCI(%d) -> %s", base.n(), algorithm.name());
        }
    }
}
