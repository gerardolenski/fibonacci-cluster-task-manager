package org.gol.taskmanager.task.infrastructure.db;

import org.gol.taskmanager.task.domain.JobPersistCmd;
import org.gol.taskmanager.task.domain.JobRepositoryPort;
import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.job.JobStats;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;
import org.gol.taskmanager.task.domain.model.task.TaskType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Builder;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

public class InMemoryTaskRepositoryAdapter implements JobRepositoryPort {

    private final Map<JobKey, JobValue> entries = new ConcurrentHashMap<>();

    @Override
    public void persist(JobPersistCmd cmd) {
        var key = new JobKey(cmd.taskId(), cmd.job().jobId());
        var value = new JobValue(cmd.taskType(), cmd.job(), null, now(), null);
        entries.put(key, value);
    }

    @Override
    public void persist(JobResult jobResult) {
        var key = new JobKey(jobResult.taskId(), jobResult.jobId());
        entries.computeIfPresent(key, (k, v) -> v.withResult(jobResult));
    }

    @Override
    public TaskStats getStatistics(TaskId taskId) {
        var allJobsCount = entries.entrySet().stream()
                .filter(e -> e.getKey().taskId().equals(taskId))
                .count();
        List<JobStats> jobs = entries.entrySet().stream()
                .filter(e -> e.getKey().taskId().equals(taskId))
                .filter(e -> Objects.nonNull(e.getValue().result()))
                .map(TaskProjection::fromEntry)
                .map(JobStats.class::cast)
                .toList();
        return new TaskStats(allJobsCount, jobs);
    }

    public List<JobId> taskJobs(TaskId taskId) {
        return entries.keySet().stream()
                .filter(j -> j.taskId.equals(taskId))
                .map(JobKey::jobId)
                .toList();
    }

    record JobKey(TaskId taskId, JobId jobId) {
    }

    record JobValue(TaskType taskType, Job job, JobResult result, LocalDateTime startTime, LocalDateTime stopTime) {

        JobValue withResult(JobResult jobResult) {
            return new JobValue(taskType, job, jobResult, startTime, now());
        }
    }

    @Builder
    record TaskProjection(
            TaskId taskId,
            TaskType taskType,
            LocalDateTime startTime,
            LocalDateTime stopTime,
            JobId jobId,
            String jobDetails,
            String jobResult,
            Long jobProcessingTime) implements JobStats {

        static TaskProjection fromEntry(Map.Entry<JobKey, JobValue> entry) {
            var key = entry.getKey();
            var value = entry.getValue();
            return TaskProjection.builder()
                    .taskId(key.taskId)
                    .taskType(value.taskType)
                    .jobId(key.jobId)
                    .startTime(value.startTime)
                    .stopTime(value.stopTime)
                    .jobDetails(value.job.jobDetails().serializedFormat())
                    .jobResult(ofNullable(value.result.result()).orElseGet(value.result::errorMessage))
                    .jobProcessingTime(value.result.processingTime())
                    .build();
        }
    }
}
