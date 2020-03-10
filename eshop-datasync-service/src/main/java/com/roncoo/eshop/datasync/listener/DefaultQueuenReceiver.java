package com.roncoo.eshop.datasync.listener;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import com.roncoo.eshop.datasync.handler.DefaultProcessDataChangeHandler;
import com.roncoo.eshop.mq.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author lzp on 2020/3/10.
 */
@Slf4j
@Component
public class DefaultQueuenReceiver extends AbstractQueueReceiver {
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private Map<String, DefaultProcessDataChangeHandler> handlers;
    @Autowired
    MessageSender messageSender;

    protected Message handlerMessage(String msg) {
        // 对这个message进行解析
        Message message = JSONObject.parseObject(msg, Message.class);
        // 先获取data_type
        String dataType = message.getDataType();
        DefaultProcessDataChangeHandler handler = handlers.get(DefaultProcessDataChangeHandler.alias(dataType));
        handler.process(message);
        return message;
    }
}
