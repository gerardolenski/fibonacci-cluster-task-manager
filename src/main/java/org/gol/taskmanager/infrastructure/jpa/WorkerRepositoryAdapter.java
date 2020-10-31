package org.gol.taskmanager.infrastructure.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.gol.taskmanager.domain.model.WorkerData;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Function;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Slf4j
@RequiredArgsConstructor
class WorkerRepositoryAdapter implements WorkerRepositoryPort {

    private static final Function<WorkerData, TaskEntity> TO_ENTITY_MAPPER = workerData ->
            TaskEntity.builder()
                    .taskId(workerData.getTaskId())
                    .startTime(LocalDateTime.now())
                    .jobId(workerData.getJobData().getJobId())
                    .jobDetails(workerData.getJobData().getJobDetails())
                    .build();
    private final TaskDao taskDao;

    @Override
    @Transactional(propagation = MANDATORY)
    public void persist(WorkerData workerData) {
        var entity = TO_ENTITY_MAPPER.apply(workerData);
        log.trace("Persisting worker data: {}", entity);
        taskDao.save(entity);
    }
}
