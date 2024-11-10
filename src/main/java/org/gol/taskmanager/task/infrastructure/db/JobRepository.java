package org.gol.taskmanager.task.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

interface JobRepository extends JpaRepository<JobEntity, UUID> {

    Long countByIdTaskId(UUID taskId);
    Stream<JobEntity> findByIdTaskIdAndStopTimeNotNullOrderByStartTime(UUID taskId);
    Optional<JobEntity> findById(JobKey id);
}
