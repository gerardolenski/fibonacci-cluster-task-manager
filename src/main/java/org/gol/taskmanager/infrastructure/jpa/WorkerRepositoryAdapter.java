package org.gol.taskmanager.infrastructure.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.gol.taskmanager.domain.model.ResultData;
import org.gol.taskmanager.domain.model.TaskStatisticsData;
import org.gol.taskmanager.domain.model.WorkerData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class WorkerRepositoryAdapter implements WorkerRepositoryPort {

    private static final Function<WorkerData, TaskEntity> TO_ENTITY = workerData ->
            TaskEntity.builder()
                    .taskId(workerData.getTaskId())
                    .startTime(now())
                    .jobId(workerData.getJobData().getJobId())
                    .jobDetails(workerData.getJobData().getJobDetails())
                    .build();

    private final Function<TaskEntity, TaskView> TO_VIEW = entity ->
            TaskView.builder()
                    .taskId(entity.getTaskId())
                    .startTime(entity.getStartTime())
                    .stopTime(entity.getStopTime())
                    .jobId(entity.getJobId())
                    .jobDetails(entity.getJobDetails())
                    .jobResult(entity.getJobResult())
                    .jobProcessingTime(entity.getJobProcessingTime())
                    .build();

    private final TaskDao taskDao;

    @Override
    @Transactional
    public void persist(WorkerData workerData) {
        var entity = TO_ENTITY.apply(workerData);
        log.trace("Persisting worker data: {}", entity);
        taskDao.save(entity);
    }

    @Override
    @Transactional
    public void persist(ResultData resultData) {
        taskDao.findByTaskIdAndJobId(resultData.getTaskId(), resultData.getJobId())
                .ifPresent(entity -> {
                    entity.setJobResult(ofNullable(resultData.getResult()).orElse(resultData.getErrorMessage()));
                    entity.setJobProcessingTime(resultData.getProcessingTime());
                    entity.setStopTime(now());
                    log.trace("Updating worker data: {}", entity);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatisticsData> getStatistics(UUID taskId) {
        return taskDao.findByTaskIdAndStopTimeNotNullOrderByStartTime(taskId)
                .map(TO_VIEW)
                .collect(toList());
    }
}
