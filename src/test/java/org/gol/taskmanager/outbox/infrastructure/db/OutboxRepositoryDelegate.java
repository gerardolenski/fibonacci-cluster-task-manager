package org.gol.taskmanager.outbox.infrastructure.db;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryDelegate {

    private final OutboxMsgRepository outboxMsgRepository;

    public void deleteAll() {
        outboxMsgRepository.deleteAll();
    }

    public long count() {
        return outboxMsgRepository.count();
    }
}
