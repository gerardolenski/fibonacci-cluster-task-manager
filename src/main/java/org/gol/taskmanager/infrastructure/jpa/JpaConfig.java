package org.gol.taskmanager.infrastructure.jpa;

import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class JpaConfig {

    @Bean
    WorkerRepositoryPort workerRepositoryAdapter(TaskDao taskDao) {
        return new WorkerRepositoryAdapter(taskDao);
    }
}
