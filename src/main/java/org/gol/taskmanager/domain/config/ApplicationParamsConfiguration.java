package org.gol.taskmanager.domain.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Getter
@ToString
@Component
class ApplicationParamsConfiguration implements ConfigurationPort {

    private final String workerQueueName;
    private final String fibResultMessageTypeId;
    private final String resultConsumerConcurrency;
    private final Set<String> fibonacciAlgorithms;

    ApplicationParamsConfiguration(
            @Value(WORKER_QUEUE_NAME_PROPERTY) String workerQueueName,
            @Value(FIB_RESULT_MESSAGE_ID_PROPERTY) String fibResultMessageId,
            @Value(RESULT_CONSUMER_CONCURRENCY_PROPERTY) String resultConsumerConcurrency,
            @Value(FIB_ALGORITHMS) Set<String> fibonacciAlgorithms) {
        this.workerQueueName = workerQueueName;
        this.fibResultMessageTypeId = fibResultMessageId;
        this.resultConsumerConcurrency = resultConsumerConcurrency;
        this.fibonacciAlgorithms = fibonacciAlgorithms;
        log.info("Init APPLICATION PARAMS configuration component: {}", this);
    }
}
