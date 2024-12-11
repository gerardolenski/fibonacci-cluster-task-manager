package org.gol.taskmanager.task.infrastructure.outbox;

import org.gol.taskmanager.outbox.application.api.MessageAddCmd;
import org.gol.taskmanager.outbox.application.api.OutboxMessagingPort;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.gol.taskmanager.task.domain.model.task.TaskType;
import org.gol.taskmanager.task.support.TestJob;
import org.gol.taskmanager.task.support.TestJobDetails;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutboxSenderTest {

    private static final UUID TASK_ID = UUID.fromString("b26d37f1-d9b9-4059-a827-b57870ad5b43");
    private static final UUID JOB_ID = UUID.fromString("b29614ac-14b3-48bb-a6c1-adacf5a4ec95");
    private static final Path CONTRACT_DIR = Path.of("src/test/resources/task/broker-contract");

    @Mock
    private OutboxMessagingPort outboxMessagingPort;
    private OutboxSender sut;

    @Captor
    ArgumentCaptor<MessageAddCmd> messageAddCmdCaptor;

    @BeforeEach
    void setUp() {
        sut = new OutboxSender(outboxMessagingPort);
    }

    @Test
    @DisplayName("should add correctly serialized message [positive]")
    void shouldAddSerializedMessage() throws IOException, JSONException {
        // given
        var taskId = new TaskId(TASK_ID);
        var jobId = new JobId(JOB_ID);
        var job = new TestJob(jobId, new TestJobDetails("FIBONACCI(5) -> ITERATIVE"));

        // when
        sut.notify(taskId, TaskType.FIBONACCI, job);

        // then
        verify(outboxMessagingPort).addMessage(messageAddCmdCaptor.capture());
        var cmd = messageAddCmdCaptor.getValue();
        assertThat(cmd.messageId()).isNotNull();
        assertThat(cmd.header()).hasValue(new MessageHeader("worker", "FIBONACCI"));
        JSONAssert.assertEquals(Files.readString(CONTRACT_DIR.resolve("job-message.json")), cmd.body().value(), false);
    }
}