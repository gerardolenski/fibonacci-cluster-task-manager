package org.gol.taskmanager.outbox.domain;

import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.outbox.domain.model.MessageId;

import java.util.Optional;

public interface NewMessageCtx {

    MessageId messageId();

    Optional<MessageHeader> header();

    MessageBody body();
}
