package com.roncoo.eshop.mq;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.BaseEntity;
import com.roncoo.eshop.common.rabbitmq.message.EventType;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageSender {

    @Autowired
    RabbitMQSender rabbitMQSender;

    public void send(String queue, Message message) {
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

    public <T> void sendAddMessage(String queue, String businessId, BaseEntity entity) {
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(entity));
        Map<String, Object> data = new HashMap<>();
        data.put("productId", jsonObject.getLongValue("productId"));
        Message<Map<String, Object>> message = new Message(EventType.ADD, businessId, entity.messageId(), data);
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

    public void sendUpdateMessage(String queue, String businessId, BaseEntity entity) {
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(entity));
        Map<String, Object> data = new HashMap<>();
        data.put("productId", jsonObject.getLongValue("productId"));
        Message<Map<String, Object>> message = new Message(EventType.UPDATE, businessId, entity.messageId(), data);
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

    public void sendDeleteMessage(String queue, String businessId, BaseEntity entity) {
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(entity));
        Map<String, Object> data = new HashMap<>();
        data.put("productId", jsonObject.getLongValue("productId"));
        Message<Map<String, Object>> message = new Message(EventType.DELETE, businessId, entity.messageId(), data);
        rabbitMQSender.send(queue, JSONObject.toJSONString(message));
    }

}
