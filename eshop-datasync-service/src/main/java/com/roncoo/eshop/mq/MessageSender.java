package com.roncoo.eshop.mq;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    RabbitMQSender rabbitMQSender;

    public void send(String queue, Message message) {
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

}
