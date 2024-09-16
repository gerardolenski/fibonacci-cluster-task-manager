package org.gol.taskmanager.application.jms;

import org.gol.taskmanager.domain.manager.WorkerRepositoryPort;
import org.gol.taskmanager.domain.model.ResultData;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.gol.taskmanager.application.jms.AmqConsumerConfig.WORKER_QUEUE_FACTORY;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultMessageAdapter {

    private static final String RESULT_SELECTOR = "workerResult = true";
    private static final String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker-queue-name}";
    private static final String RESULT_CONSUMER_CONCURRENCY_PROPERTY = "${mq.result-consumer.concurrency}";
    
    private final WorkerRepositoryPort repositoryPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = RESULT_SELECTOR,
            concurrency = RESULT_CONSUMER_CONCURRENCY_PROPERTY,
            containerFactory = WORKER_QUEUE_FACTORY)
    void handleResultMessage(Message<ResultData> message) {
        var resultData = message.getPayload();
        log.debug("Received message of taskId={} and jobId={}", resultData.taskId(), resultData.jobId());
        log.trace("Message details: {}", resultData);
        repositoryPort.persist(resultData);
    }
}
