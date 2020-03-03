package com.roncoo.eshop.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {

    @Autowired
    protected AmqpTemplate rabbitTemplate;

    protected void send(String queue, String message) {
        this.rabbitTemplate.convertAndSend(queue, message);
    }

}