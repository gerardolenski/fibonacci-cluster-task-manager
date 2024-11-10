package org.gol.taskmanager.outbox.application.service;

import org.gol.taskmanager.outbox.application.api.MessageAddCmd;
import org.gol.taskmanager.outbox.application.api.OutboxMessagingPort;
import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Acceptance test of module use cases.
 */
class OutboxServiceTest {

    private OutboxMessagingPort sut;
    private OutboxModuleTestConfigurer config;

    @BeforeEach
    void setUp() {
        config = new OutboxModuleTestConfigurer();
        sut = config.outboxService();
    }

    @Test
    @DisplayName("should correctly run 'add message' use case flow [positive]")
    void addMessageUseCase() {
        //given
        var cmd = MessageAddCmd.builder()
                .header(new MessageHeader("worker", "test"))
                .body(new MessageBody("test message body"))
                .build();

        //when
        var msgId = sut.addMessage(cmd);

        //then
        var repo = config.persistAdapter();
        assertThat(repo.getMessage(msgId))
                .isNotEmpty()
                .hasValueSatisfying(m -> assertThat(m.headers()).containsExactly(new MessageHeader("worker", "test")))
                .hasValueSatisfying(m -> assertThat(m.body()).isEqualTo(new MessageBody("test message body")));
    }
}