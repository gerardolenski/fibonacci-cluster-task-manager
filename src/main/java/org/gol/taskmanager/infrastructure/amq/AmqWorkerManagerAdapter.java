package org.gol.taskmanager.infrastructure.amq;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.gol.taskmanager.domain.manager.WorkerNotificationPort;
import org.gol.taskmanager.domain.model.WorkerData;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class AmqWorkerManagerAdapter implements WorkerNotificationPort {

    public static final String WORKER_PROPERTY_NAME = "worker";

    private static final Function<WorkerData, AmqWorkerMessage> TO_MESSAGE = workerData ->
            new AmqWorkerMessage(workerData.taskId().toString(), workerData.jobData());

    private static final Function<WorkerData, MessagePostProcessor> TO_WORKER_PROPERTY_POSTPROCESSOR = worker ->
            message -> {
                message.setStringProperty(WORKER_PROPERTY_NAME, worker.workerType().name());
                return message;
            };

    private final JmsTemplate jmsTemplate;
    private final String queueName;

    @Override
    public void notify(WorkerData workerData) {
        var message = TO_MESSAGE.apply(workerData);
        log.trace("Sending worker message: {} -> {}", workerData.workerType(), message);
        jmsTemplate.convertAndSend(
                new ActiveMQQueue(queueName),
                message,
                TO_WORKER_PROPERTY_POSTPROCESSOR.apply(workerData));
    }
}
