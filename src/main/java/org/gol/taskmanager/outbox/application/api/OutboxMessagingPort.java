package org.gol.taskmanager.outbox.application.api;

import org.gol.taskmanager.outbox.domain.model.MessageId;

/**
 * Primary port to implement outbox messaging use cases.
 */
public interface OutboxMessagingPort {

    MessageId addMessage(MessageAddCmd cmd);
}
