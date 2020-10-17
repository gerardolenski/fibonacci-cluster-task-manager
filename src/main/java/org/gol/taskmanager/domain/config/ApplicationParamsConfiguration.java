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
    public static final String WORKER_QUEUE_NAME = "${mq.worker-queue-name}";

    private final String workerQueueName;

    ApplicationParamsConfiguration(@Value(WORKER_QUEUE_NAME) String workerQueueName) {
        this.workerQueueName = workerQueueName;
        log.info("Init APPLICATION PARAMS configuration component: {}", this);
    }
}
