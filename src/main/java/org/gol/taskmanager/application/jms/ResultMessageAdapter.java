package org.gol.taskmanager.application.jms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.config.ConfigurationPort;
import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.gol.taskmanager.domain.model.ResultData;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.gol.taskmanager.application.jms.AmqConsumerConfig.WORKER_QUEUE_FACTORY;
import static org.gol.taskmanager.domain.config.ConfigurationPort.RESULT_CONSUMER_CONCURRENCY_PROPERTY;
import static org.gol.taskmanager.domain.config.ConfigurationPort.WORKER_QUEUE_NAME_PROPERTY;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultMessageAdapter {

    private static final String RESULT_SELECTOR = "workerResult = true";

    private final ConfigurationPort configurationPort;
    private final WorkerRepositoryPort repositoryPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = RESULT_SELECTOR,
            concurrency = RESULT_CONSUMER_CONCURRENCY_PROPERTY,
            containerFactory = WORKER_QUEUE_FACTORY)
    void handleResultMessage(Message<ResultData> message) {
        var resultData = message.getPayload();
        log.debug("Received message of taskId={} and jobId={}", resultData.getTaskId(), resultData.getJobId());
        log.trace("Message details: {}", resultData);
        repositoryPort.persist(resultData);
    }

    @PostConstruct
    void init() {
        log.info("Initialized RESULT MESSAGE LISTENER with queue: {} and concurrency: {}",
                configurationPort.getWorkerQueueName(), configurationPort.getResultConsumerConcurrency());
    }
}
