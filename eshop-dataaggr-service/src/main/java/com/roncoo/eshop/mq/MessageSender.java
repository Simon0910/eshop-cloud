package com.roncoo.eshop.mq;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.BaseEntity;
import com.roncoo.eshop.common.rabbitmq.message.EventType;
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

    public void sendAddMessage(String queue, String businessId, BaseEntity entity) {
        Message message = new Message(EventType.ADD, businessId, entity.messageId());
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

    public void sendUpdateMessage(String queue, String businessId, BaseEntity entity) {
        Message message = new Message(EventType.UPDATE, businessId, entity.messageId());
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

    public void sendDeleteMessage(String queue, String businessId, BaseEntity entity) {
        Message message = new Message(EventType.DELETE, businessId, entity.messageId());
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

}
