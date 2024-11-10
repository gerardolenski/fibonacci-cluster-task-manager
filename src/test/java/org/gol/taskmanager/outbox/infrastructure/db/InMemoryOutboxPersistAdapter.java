package org.gol.taskmanager.outbox.infrastructure.db;

import org.gol.taskmanager.outbox.domain.NewMessageCtx;
import org.gol.taskmanager.outbox.domain.OutboxPersistPort;
import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.outbox.domain.model.MessageId;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOutboxPersistAdapter implements OutboxPersistPort {

    private final Map<MessageId, MessageData> messages = new ConcurrentHashMap<>();

    @Override
    public void persistNewMessage(NewMessageCtx msgCtx) {
        var headers = msgCtx.header()
                .map(List::of)
                .orElseGet(List::of);
        messages.put(msgCtx.messageId(), new MessageData(headers, msgCtx.body()));
    }

    public Optional<MessageData> getMessage(MessageId messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    public List<MessageData> getAllMessages() {
        return messages.values().stream()
                .toList();
    }

    public record MessageData(List<MessageHeader> headers, MessageBody body) {
    }
}
