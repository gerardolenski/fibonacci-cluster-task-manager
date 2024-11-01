package org.gol.taskmanager.outbox.domain;

public interface OutboxPersistPort {

    void persistNewMessage(NewMessageCtx msgCtx);
}
