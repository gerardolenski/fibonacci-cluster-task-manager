package org.gol.taskmanager.task.infrastructure.outbox;

import org.gol.taskmanager.outbox.application.api.OutboxMessagingPort;
import org.gol.taskmanager.task.domain.JobNotificationPort;

public class OutboxSenderFactory {

    public JobNotificationPort outboxSender(OutboxMessagingPort outboxMessagingPort) {
        return new OutboxSender(outboxMessagingPort);
    }
}
