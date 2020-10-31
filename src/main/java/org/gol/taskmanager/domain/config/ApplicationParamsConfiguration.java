package org.gol.taskmanager.domain.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@ToString
@Component
class ApplicationParamsConfiguration implements ConfigurationPort {

    private final String workerQueueName;
    private final String fibResultMessageTypeId;
    private final String resultConsumerConcurrency;

    ApplicationParamsConfiguration(
            @Value(WORKER_QUEUE_NAME_PROPERTY) String workerQueueName,
            @Value(FIB_RESULT_MESSAGE_ID_PROPERTY) String fibResultMessageId,
            @Value(RESULT_CONSUMER_CONCURRENCY_PROPERTY) String resultConsumerConcurrency) {
        this.workerQueueName = workerQueueName;
        this.fibResultMessageTypeId = fibResultMessageId;
        this.resultConsumerConcurrency = resultConsumerConcurrency;
        log.info("Init APPLICATION PARAMS configuration component: {}", this);
    }
}
