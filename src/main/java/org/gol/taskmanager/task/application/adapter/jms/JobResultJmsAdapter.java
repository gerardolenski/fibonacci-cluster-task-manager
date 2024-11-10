package org.gol.taskmanager.task.application.adapter.jms;

import com.google.gson.Gson;

import org.gol.taskmanager.task.application.api.TaskManagementPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import io.vavr.control.Try;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
class JobResultJmsAdapter {

    private static final String JOB_RESULT_QUEUE_NAME = "${mq.job-result-queue-name}";
    private static final String RESULT_CONSUMER_CONCURRENCY = "${mq.result-consumer.concurrency}";
    private static final Gson GSON = new Gson();

    private final TaskManagementPort taskManagementPort;

    @JmsListener(destination = JOB_RESULT_QUEUE_NAME, concurrency = RESULT_CONSUMER_CONCURRENCY)
    void handleResultMessage(TextMessage message) {
        parseMessage(message)
                .ifPresent(this::processMessage);
    }

    private Optional<JobResultMessage> parseMessage(TextMessage message) {
        return Try.of(message::getText)
                .map(payload -> GSON.fromJson(payload, JobResultMessage.class))
                .andThen(this::validateMsg)
                .map(Optional::of)
                .onFailure(this::logParseError)
                .getOrElse(Optional::empty);
    }

    private void validateMsg(JobResultMessage msg) {
        if (isNull(msg.taskId()))
            throw new IllegalArgumentException("Task value is mandatory");
        if (isNull(msg.jobId()))
            throw new IllegalArgumentException("Job value is mandatory");

    }

    private void processMessage(JobResultMessage resultMsg) {
        log.info("Received result message: taskId={}, jobId={}", resultMsg.taskId(), resultMsg.jobId());
        log.debug("Message details: {}", resultMsg);
        taskManagementPort.registerResult(resultMsg);
    }

    void logParseError(Throwable e) {
        log.error("The result message could not be parsed", e);
    }
}
