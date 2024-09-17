package org.gol.taskmanager.application.jms;

import com.google.gson.Gson;

import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import io.vavr.control.Try;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FibResultMessageAdapter {

    private static final String RESULT_SELECTOR = "workerResult = true";
    private static final String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker-queue-name}";
    private static final String RESULT_CONSUMER_CONCURRENCY_PROPERTY = "${mq.result-consumer.concurrency}";
    private static final Gson GSON = new Gson();

    private final WorkerRepositoryPort repositoryPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = RESULT_SELECTOR,
            concurrency = RESULT_CONSUMER_CONCURRENCY_PROPERTY)
    void handleResultMessage(TextMessage message) {
        parseMessage(message)
                .ifPresent(this::processMessage);
    }

    private void processMessage(FibResultMessage fibResult) {
        log.debug("Received Fibonacci result message: taskId={}, jobId={}", fibResult.taskId(), fibResult.jobId());
        log.trace("Message details: {}", fibResult);
        repositoryPort.persist(fibResult);
    }

    private Optional<FibResultMessage> parseMessage(TextMessage message) {
        return Try.of(message::getText)
                .map(payload -> GSON.fromJson(payload, FibResultMessage.class))
                .map(Optional::of)
                .onFailure(e -> log.error("The message could not be parsed", e))
                .getOrElse(Optional::empty);
    }
}
