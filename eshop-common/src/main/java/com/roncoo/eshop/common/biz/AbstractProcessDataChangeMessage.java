package com.roncoo.eshop.common.biz;

import com.roncoo.eshop.common.rabbitmq.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProcessDataChangeMessage {

    protected static Map<String, String> aliasMap = new ConcurrentHashMap<>();

    protected abstract void processAddOrUpdate(Message message);

    protected abstract void processDelete(Message message);

    public static String alias(String business) {
        return aliasMap.get(business);
    }
}
