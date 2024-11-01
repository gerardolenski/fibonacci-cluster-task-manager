package org.gol.taskmanager.task.application.adapter.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.gol.taskmanager.BaseIT;
import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.gol.taskmanager.task.domain.model.job.JobId;
import org.gol.taskmanager.task.domain.model.job.JobResult;
import org.gol.taskmanager.task.domain.model.task.TaskId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.Files.readString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@TestPropertySource(properties = "mq.job-result-queue-name=JobResultJmsAdapterTest")
class JobResultJmsAdapterTest extends BaseIT {

    private static final Path CONTRACT_DIR = Path.of("src/test/resources/task/broker-contract");
    private static final long MAX_TIMEOUT = 10_000;
    private static final TaskId TASK_ID = new TaskId(UUID.fromString("f804a4bc-f533-4aa0-8eca-b0fc25ebc3f8"));

    @SpyBean
    private JobResultJmsAdapter sut;
    @MockBean
    private TaskManagementPort taskManagementPort;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Captor
    private ArgumentCaptor<JobResult> jobResultCaptor;


    @Test
    void successMessage() throws IOException {
        //when
        sendResultMessage("job-result-message-success.json");

        //then
        verify(taskManagementPort, timeout(MAX_TIMEOUT)).registerResult(jobResultCaptor.capture());
        assertThat(jobResultCaptor.getValue())
                .satisfies(r -> assertThat(r.taskId()).isEqualTo(TASK_ID))
                .satisfies(r -> assertThat(r.jobId()).isEqualTo(new JobId(UUID.fromString("fa2b46f9-f274-4a10-9181-44a8c95591ea"))))
                .satisfies(r -> assertThat(r.result()).isEqualTo("55"))
                .satisfies(r -> assertThat(r.processingTime()).isEqualTo(1255))
                .satisfies(r -> assertThat(r.errorMessage()).isNull());
    }

    @Test
    void failureMessage() throws IOException {
        //when
        sendResultMessage("job-result-message-failure.json");

        //then
        verify(taskManagementPort, timeout(MAX_TIMEOUT)).registerResult(jobResultCaptor.capture());
        assertThat(jobResultCaptor.getValue())
                .satisfies(r -> assertThat(r.taskId()).isEqualTo(TASK_ID))
                .satisfies(r -> assertThat(r.jobId()).isEqualTo(new JobId(UUID.fromString("fa2b46f9-f274-4a10-9181-44a8c95591ea"))))
                .satisfies(r -> assertThat(r.result()).isNull())
                .satisfies(r -> assertThat(r.processingTime()).isEqualTo(333))
                .satisfies(r -> assertThat(r.errorMessage()).isEqualTo("The job failed during computation"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"job-result-message-no-ids.json", "job-result-message-not-json"})
    void corruptedMessages(String contractFileName) throws IOException {
        sendResultMessage(contractFileName);
        verify(sut, timeout(MAX_TIMEOUT)).logParseError(any());
        verify(taskManagementPort, never()).registerResult(any());
    }

    private void sendResultMessage(String messageFileName) throws IOException {
        var messagePayload = readString(CONTRACT_DIR.resolve(messageFileName));

        jmsTemplate.convertAndSend(new ActiveMQQueue("JobResultJmsAdapterTest"),
                messagePayload,
                msg -> {
                    msg.setStringProperty("type", "FIBONACCI");
                    return msg;
                });
    }
}