package org.gol.taskmanager.outbox.infrastructure.db;

import org.gol.taskmanager.BaseIT;
import org.gol.taskmanager.outbox.application.api.MessageAddCmd;
import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.outbox.domain.model.MessageId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.assertj.core.api.Assertions.assertThat;
import static org.gol.taskmanager.task.domain.model.task.TaskType.FIBONACCI;

class OutboxJpaAdapterTest extends BaseIT {

    @Autowired
    private OutboxJpaAdapter sut;
    @Autowired
    private OutboxMsgRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("should persist outbox message with header [positive]")
    void shouldPersist() {
        //given
        var cmd = MessageAddCmd.builder()
                .header(new MessageHeader("worker", FIBONACCI.name()))
                .body(new MessageBody("message body"))
                .build();

        //when
        sut.persistNewMessage(cmd);

        //then
        var entity = repository.findById(cmd.messageId().value());
        assertThat(entity)
                .isNotEmpty()
                .hasValueSatisfying(e -> assertThat(e.getHeaders())
                        .singleElement()
                        .extracting(OutboxMsgHeader::getKey, OutboxMsgHeader::getValue)
                        .containsExactly("worker", FIBONACCI.name()))
                .hasValueSatisfying(e -> assertThat(e.getData()).isEqualTo("message body"))
                .hasValueSatisfying(e -> assertThat(e.getIsDelivered()).isFalse())
                .hasValueSatisfying(e -> assertThat(e.getCreatedAt()).isNotNull())
                .hasValueSatisfying(e -> assertThat(e.getModifiedAt()).isNotNull());
    }

    @Test
    @DisplayName("should persist outbox message without header [positive]")
    void shouldPersistMsgWithoutHeader() {
        //given
        var cmd = MessageAddCmd.builder()
                .body(new MessageBody("message body"))
                .build();

        //when
        sut.persistNewMessage(cmd);

        //then
        var entity = repository.findById(cmd.messageId().value());
        assertThat(entity)
                .isNotEmpty()
                .hasValueSatisfying(e -> assertThat(e.getHeaders()).isEmpty())
                .hasValueSatisfying(e -> assertThat(e.getData()).isEqualTo("message body"))
                .hasValueSatisfying(e -> assertThat(e.getIsDelivered()).isFalse())
                .hasValueSatisfying(e -> assertThat(e.getCreatedAt()).isNotNull())
                .hasValueSatisfying(e -> assertThat(e.getModifiedAt()).isNotNull());
    }

    @Test
    @DisplayName("should persist outbox message with header [positive]")
    void shouldPersistConcurrently() {
        //given
        var executor = newFixedThreadPool(3);
        var cmds = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> MessageAddCmd.builder()
                        .header(new MessageHeader("worker", FIBONACCI.name()))
                        .body(new MessageBody("message body " + i))
                        .build())
                .toList();

        //when
        cmds.stream()
                .map(cmd -> CompletableFuture.runAsync(() -> sut.persistNewMessage(cmd), executor))
                .forEach(CompletableFuture::join);

        //then
        assertThat(repository.findAll())
                .extracting(OutboxMsgEntity::getId)
                .containsExactlyInAnyOrderElementsOf(cmds.stream()
                        .map(MessageAddCmd::messageId)
                        .map(MessageId::value)
                        .toList());
    }
}