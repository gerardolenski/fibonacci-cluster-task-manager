package org.gol.taskmanager.task.application.service;

import org.gol.taskmanager.outbox.application.service.OutboxModuleTestConfigurer;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.JobsScheduler;
import org.gol.taskmanager.task.infrastructure.db.InMemoryTaskRepositoryAdapter;
import org.gol.taskmanager.task.infrastructure.outbox.OutboxSenderFactory;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class TaskModuleTestConfigurer {

    private final OutboxModuleTestConfigurer outboxConfig;
    private final InMemoryTaskRepositoryAdapter taskRepository;
    private final OutboxSenderFactory outboxSenderFactory;
    private final TaskManagementPort taskManager;

    public TaskModuleTestConfigurer() {
        outboxConfig = new OutboxModuleTestConfigurer();
        taskRepository = new InMemoryTaskRepositoryAdapter();
        outboxSenderFactory = new OutboxSenderFactory();

        var notificationPort = outboxSenderFactory.outboxSender(outboxConfig.outboxService());
        taskManager = TaskManager.builder()
                .jobsScheduler(JobsScheduler.builder()
                        .repositoryPort(taskRepository)
                        .notificationPort(notificationPort)
                        .build())
                .repositoryPort(taskRepository)
                .build();

    }

    public TaskModuleSteps steps() {
        return new TaskModuleSteps(this);
    }
}
