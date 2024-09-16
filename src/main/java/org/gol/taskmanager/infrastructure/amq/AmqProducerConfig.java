package org.gol.taskmanager.infrastructure.amq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.gol.taskmanager.domain.manager.WorkerNotificationPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import jakarta.jms.ConnectionFactory;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static org.springframework.jms.support.converter.MessageType.TEXT;

@Configuration
class AmqProducerConfig {

    private static final String TYPE_ID_PROPERTY_NAME = "_type";

    @Bean
    WorkerNotificationPort amqWorkerManagerAdapter(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${mq.worker-queue-name}") String workerQueueName) {
        return new AmqWorkerManagerAdapter(initJmsTemplate(connectionFactory), workerQueueName);
    }

    private JmsTemplate initJmsTemplate(ConnectionFactory connectionFactory) {
        var jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(initJmsMessageConverter());
        return jmsTemplate;
    }

    private MessageConverter initJmsMessageConverter() {
        var converter = new MappingJackson2MessageConverter();
        converter.setTargetType(TEXT);
        converter.setTypeIdPropertyName(TYPE_ID_PROPERTY_NAME);
        converter.setObjectMapper(initJmsObjectMapper());
        return converter;
    }

    private ObjectMapper initJmsObjectMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule(PROPERTIES));
    }
}
