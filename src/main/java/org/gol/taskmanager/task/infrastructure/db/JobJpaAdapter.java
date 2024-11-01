package org.gol.taskmanager.task.infrastructure.db;

import org.gol.taskmanager.task.domain.JobPersistCmd;
import org.gol.taskmanager.task.domain.JobRepositoryPort;
import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.job.JobStats;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskStats;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class JobJpaAdapter implements JobRepositoryPort {

    private static final Function<JobPersistCmd, JobEntity> TO_ENTITY = cmd ->
            JobEntity.builder()
                    .id(new JobKey(cmd.taskId().value(), cmd.job().jobId().value()))
                    .type(cmd.taskType())
                    .startTime(now())
                    .jobDetails(cmd.job().jobDetails().serializedFormat())
                    .build();

    private static final Function<JobEntity, JobProjection> TO_JOB_STATS = entity ->
            JobProjection.builder()
                    .taskId(new TaskId(entity.getId().getTaskId()))
                    .taskType(entity.getType())
                    .jobId(new JobId(entity.getId().getJobId()))
                    .startTime(entity.getStartTime())
                    .stopTime(entity.getStopTime())
                    .jobDetails(entity.getJobDetails())
                    .jobResult(entity.getJobResult())
                    .jobProcessingTime(entity.getJobProcessingTime())
                    .build();

    private final JobRepository jobRepository;

    @Override
    @Transactional
    public void persist(JobPersistCmd cmd) {
        var entity = TO_ENTITY.apply(cmd);
        log.trace("Persisting job data: {}", entity);
        jobRepository.save(entity);
    }

    @Override
    @Transactional
    public void persist(JobResult jobResult) {
        jobRepository.findById(new JobKey(jobResult.taskId().value(), jobResult.jobId().value()))
                .ifPresent(entity -> {
                    entity.setJobResult(ofNullable(jobResult.result()).orElseGet(jobResult::errorMessage));
                    entity.setJobProcessingTime(jobResult.processingTime());
                    entity.setStopTime(now());
                    log.trace("Updating job data: {}", entity);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStats getStatistics(TaskId taskId) {
        var scheduledJobsCount = jobRepository.countByIdTaskId(taskId.value());
        List<JobStats> allJobsStats = jobRepository.findByIdTaskIdAndStopTimeNotNullOrderByStartTime(taskId.value())
                .map(TO_JOB_STATS)
                .collect(toList());
        return new TaskStats(scheduledJobsCount, allJobsStats);
    }
}
