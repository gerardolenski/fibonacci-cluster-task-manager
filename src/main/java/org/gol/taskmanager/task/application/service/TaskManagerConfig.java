package org.gol.taskmanager.task.application.service;

import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.JobNotificationPort;
import org.gol.taskmanager.task.domain.JobRepositoryPort;
import org.gol.taskmanager.task.domain.JobsScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
class TaskManagerConfig {

    @Bean
    TaskManagementPort workerManagerAdapter(
            JobNotificationPort jobNotificationPort,
            JobRepositoryPort jobRepositoryPort) {
        return TaskManager.builder()
                .jobsScheduler(JobsScheduler.builder()
                        .repositoryPort(jobRepositoryPort)
                        .notificationPort(jobNotificationPort)
                        .build())
                .repositoryPort(jobRepositoryPort)
                .build();
    }
}
