package org.gol.taskmanager.domain.manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
class WorkerConfig {

    @Bean
    WorkerManagerPort workerManagerAdapter(
            WorkerNotificationPort workerNotificationPort,
            WorkerRepositoryPort workerRepositoryPort) {
        return new WorkerManager(workerNotificationPort, workerRepositoryPort);
    }
}
