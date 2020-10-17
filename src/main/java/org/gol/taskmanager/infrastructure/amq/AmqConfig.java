package org.gol.taskmanager.infrastructure.amq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.taskmanager.domain.config.ConfigurationPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

import static org.springframework.jms.support.converter.MessageType.TEXT;

@Slf4j
@Configuration
class AmqConfig {

    private static final String TYPE_ID_PROPERTY_NAME = "_type";

    @Bean("jacksonJmsMessageConverter")
    MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(TEXT);
        converter.setTypeIdPropertyName(TYPE_ID_PROPERTY_NAME);
        return converter;
    }

    @Bean
    JmsTemplate jmsTemplate(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
            @Qualifier("jacksonJmsMessageConverter") MessageConverter jacksonJmsMessageConverter) {
        var jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
        return jmsTemplate;
    }

    @Bean
    AmqWorkerManagerAdapter amqWorkerManagerAdapter(JmsTemplate jmsTemplate, ConfigurationPort configurationPort) {
        return new AmqWorkerManagerAdapter(jmsTemplate, configurationPort.getWorkerQueueName());
    }
}
