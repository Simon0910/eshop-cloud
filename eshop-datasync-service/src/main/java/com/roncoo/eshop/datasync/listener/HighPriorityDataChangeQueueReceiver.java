package com.roncoo.eshop.datasync.listener;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
// @Component
// @RabbitListener(queues = "high-priority-data-change-queue")
public class HighPriorityDataChangeQueueReceiver extends DefaultQueuenReceiver {

    private Set<Message> dimDataChangeMessageSet = Collections.synchronizedSet(new HashSet<Message>());

    public HighPriorityDataChangeQueueReceiver() {
        new SendThread().start();
    }

    @RabbitHandler
    public void process(String msg) {
        try {
            Message message = handlerMessage(msg);
            log.info("【维度数据变更消息被放入内存Set中】,message = {}", JSONObject.toJSONString(message));
            dimDataChangeMessageSet.add(message);
        } catch (Exception e) {
            log.error("error - msg = {}, e : {}", msg, e);
            return;
        }
    }

    private class SendThread extends Thread {

        @Override
        public void run() {
            while (true) {
                if (!dimDataChangeMessageSet.isEmpty()) {
                    for (Message message : dimDataChangeMessageSet) {
                        messageSender.send(RabbitQueue.HIGH_PRIORITY_AGGR_DATA_CHANGE_QUEUE, message);
						log.info("【将去重后的维度数据变更消息发送到下一个queue】,message=" + message);
                    }
                    dimDataChangeMessageSet.clear();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}  