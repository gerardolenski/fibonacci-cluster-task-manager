package org.gol.taskmanager.outbox.application.service;

import org.gol.taskmanager.outbox.application.api.OutboxMessagingPort;
import org.gol.taskmanager.outbox.infrastructure.db.InMemoryOutboxPersistAdapter;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class OutboxModuleTestConfigurer {

    private final InMemoryOutboxPersistAdapter persistAdapter;
    private final OutboxMessagingPort outboxService;

    public OutboxModuleTestConfigurer() {
        persistAdapter = new InMemoryOutboxPersistAdapter();
        this.outboxService = new OutboxService(persistAdapter);
    }

    public OutboxModuleSteps steps() {
        return new OutboxModuleSteps(this);
    }


}
