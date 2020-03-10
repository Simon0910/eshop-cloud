package com.roncoo.eshop.datasync.listener;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import com.roncoo.eshop.datasync.handler.DefaultProcessDataChangeHandler;
import com.roncoo.eshop.datasync.service.EshopProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 数据同步服务，就是获取各种原子数据的变更消息
 * <p>
 * （1）然后通过spring cloud fegion调用eshop-product-service服务的各种接口， 获取数据
 * （2）将原子数据在redis中进行增删改
 * （3）将维度数据变化消息写入rabbitmq中另外一个queue，供数据聚合服务来消费
 *
 * @author Administrator
 */
@Slf4j
@Component
@RabbitListener(queues = "data-change-queue")
public class DataChangeQueueReceiver {
    @Autowired
    private EshopProductService eshopProductService;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private Map<String, DefaultProcessDataChangeHandler> handlers;


    @RabbitHandler
    public void process(String msg) {
        try {
            // 对这个message进行解析
            Message message = JSONObject.parseObject(msg, Message.class);
            // 先获取data_type
            String dataType = message.getDataType();
            DefaultProcessDataChangeHandler handler = handlers.get(DefaultProcessDataChangeHandler.alias(dataType));
            handler.process(message);
        } catch (Exception e) {
            log.error("error - msg = {}, e : {}", msg, e);
            return;
        }
    }

}