package org.gol.taskmanager.outbox.application.service;

import org.gol.taskmanager.outbox.application.api.MessageAddCmd;
import org.gol.taskmanager.outbox.application.api.OutboxMessagingPort;
import org.gol.taskmanager.outbox.domain.OutboxPersistPort;
import org.gol.taskmanager.outbox.domain.model.MessageId;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The application Service with outbox module use cases.
 */
@Slf4j
@Service
@RequiredArgsConstructor
class OutboxService implements OutboxMessagingPort {

    private final OutboxPersistPort outboxPersistPort;

    @Override
    public MessageId addMessage(MessageAddCmd cmd) {
        log.info("Adding new message to outbox: {}", cmd);
        outboxPersistPort.persistNewMessage(cmd);
        return cmd.messageId();
    }
}
