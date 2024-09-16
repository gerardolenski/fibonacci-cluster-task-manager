package org.gol.taskmanager.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    Stream<TaskEntity> findByTaskIdAndStopTimeNotNullOrderByStartTime(UUID taskId);
    Optional<TaskEntity> findByTaskIdAndJobId(UUID taskId, UUID jobId);
}
