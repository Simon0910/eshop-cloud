package com.roncoo.eshop.dataaggr.handler;

import com.roncoo.eshop.common.biz.AbstractProcessDataChangeMessage;
import com.roncoo.eshop.common.rabbitmq.message.EventType;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultAggregationHandler extends AbstractProcessDataChangeMessage {

    @Autowired
    RedisService redisService;
    @Autowired
    MessageSender messageSender;

    @Override
    protected void processAddOrUpdate(Message message) {

    }

    @Override
    protected void processDelete(Message message) {

    }

    public void process(Message message) {
        String eventType = message.getEventType();
        if (EventType.ADD.equals(eventType) || EventType.UPDATE.equals(eventType)) {
            processAddOrUpdate(message);
        } else if (EventType.DELETE.equals(eventType)) {
            processDelete(message);
        }
    }

}
