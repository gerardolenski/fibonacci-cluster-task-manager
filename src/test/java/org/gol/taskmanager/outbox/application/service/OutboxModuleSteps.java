package org.gol.taskmanager.outbox.application.service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxModuleSteps {

    private final OutboxModuleTestConfigurer configurer;

    public int outboxMessagesCount() {
        return configurer.persistAdapter().getAllMessages().size();
    }
}
