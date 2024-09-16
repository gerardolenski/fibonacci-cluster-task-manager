package org.gol.taskmanager.infrastructure.jpa;

import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
class JpaConfig {

    @Bean
    WorkerRepositoryPort workerRepositoryAdapter(TaskRepository taskRepository) {
        return new WorkerRepositoryAdapter(taskRepository);
    }
}
