package org.gol.taskmanager.task.infrastructure.outbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.gol.taskmanager.outbox.application.api.MessageAddCmd;
import org.gol.taskmanager.outbox.application.api.OutboxMessagingPort;
import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.task.domain.JobNotificationPort;
import org.gol.taskmanager.task.domain.model.job.Job;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OutboxSender implements JobNotificationPort {

    private static final String WORKER_PROPERTY_NAME = "worker";
    private static final Gson GSON = new GsonBuilder().create();

    private final OutboxMessagingPort outboxMessagingPort;

    @Override
    public void notify(TaskId taskId, TaskType taskType, Job job) {
        var msgBody = new TaskMessage(taskId.value().toString(),
                job.jobId().value().toString(),
                job.jobDetails().serializedFormat());
        var cmd = MessageAddCmd.builder()
                .header(new MessageHeader(WORKER_PROPERTY_NAME, taskType.name()))
                .body(new MessageBody(GSON.toJson(msgBody)))
                .build();
        outboxMessagingPort.addMessage(cmd);
    }
}
