package org.gol.taskmanager.outbox.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxMsgRepository extends JpaRepository<OutboxMsgEntity, UUID> {
}
