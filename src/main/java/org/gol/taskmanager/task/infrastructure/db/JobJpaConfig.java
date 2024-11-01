package org.gol.taskmanager.task.infrastructure.db;

import org.gol.taskmanager.task.domain.JobRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
class JobJpaConfig {

    @Bean
    JobRepositoryPort workerRepositoryAdapter(JobRepository jobRepository) {
        return new JobJpaAdapter(jobRepository);
    }
}
