package org.gol.taskmanager.domain.config;

public interface ConfigurationPort {
    String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker-queue-name}";
    String RESULT_CONSUMER_CONCURRENCY_PROPERTY = "${mq.result-consumer.concurrency}";
    String FIB_RESULT_MESSAGE_ID_PROPERTY = "${mq.result-consumer.fib-id}";

    String getWorkerQueueName();
    String getResultConsumerConcurrency();
    String getFibResultMessageTypeId();
}
