package org.gol.taskmanager.outbox.application.api;

import org.gol.taskmanager.outbox.domain.NewMessageCtx;
import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.outbox.domain.model.MessageId;

import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import static org.gol.taskmanager.outbox.domain.model.MessageId.generateMsgId;

@Getter
@ToString
@Accessors(fluent = true)
public class MessageAddCmd implements NewMessageCtx {

    private final MessageId messageId;
    private final MessageHeader header;
    private final MessageBody body;

    @Builder
    MessageAddCmd(MessageHeader header, @NonNull MessageBody body) {
        this.messageId = generateMsgId();
        this.header = header;
        this.body = body;
    }

    public Optional<MessageHeader> header() {
        return Optional.ofNullable(header);
    }
}
