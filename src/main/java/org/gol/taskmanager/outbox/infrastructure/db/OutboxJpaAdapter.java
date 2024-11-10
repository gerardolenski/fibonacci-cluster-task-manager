package org.gol.taskmanager.outbox.infrastructure.db;

import org.gol.taskmanager.outbox.domain.NewMessageCtx;
import org.gol.taskmanager.outbox.domain.OutboxPersistPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.gol.taskmanager.outbox.infrastructure.db.OutboxMsgEntity.createOutboxMsg;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class OutboxJpaAdapter implements OutboxPersistPort {

    private final OutboxMsgRepository repository;

    @Override
    public void persistNewMessage(NewMessageCtx cmd) {
        log.trace("Persisting new outbox message: {}", cmd);
        var msg = createOutboxMsg(cmd.messageId(), cmd.header().orElse(null), cmd.body());
        var entity = repository.save(msg);
        log.trace("Persisted entity: {}", msg);
    }
}
