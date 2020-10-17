package org.gol.taskmanager.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.stream.Stream;

interface TaskDao extends JpaRepository<TaskEntity, UUID> {

    Stream<TaskEntity> findByTaskIdOrderByStartTime(UUID taskId);
}
