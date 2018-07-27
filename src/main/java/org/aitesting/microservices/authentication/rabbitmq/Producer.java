package org.aitesting.microservices.authentication.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    TopicExchange topicExchange;

    public void send(String message) {
        log.info(String.format("Sending message %s to %s", message, topicExchange.getName()));
        this.rabbitTemplate.convertAndSend(topicExchange.getName(), "notification.user.userauthenticated", message);
    }
}
