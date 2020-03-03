package com.roncoo.eshop.dataaggr.listener;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import com.roncoo.eshop.dataaggr.biz.ProductAggregationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "aggr-data-change-queue")
public class AggrDataChangeQueueReceiver {

    @Autowired
    ProductAggregationHandler processProduct;

    @RabbitHandler
    public void process(String msg) {
        try {
            Message message = JSONObject.parseObject(msg, Message.class);

            if (Business.PRODUCT_CHANGE.equals(message.getDataType())) {
                processProduct.process(message);
            }
        } catch (Exception e) {
            log.error("error - msg = {}, e : {}", msg, e);
            return;
        }


    }

}