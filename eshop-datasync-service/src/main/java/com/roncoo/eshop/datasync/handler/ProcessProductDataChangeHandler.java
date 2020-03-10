package com.roncoo.eshop.datasync.handler;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component(value = "processProductDataChangeHandler")
public class ProcessProductDataChangeHandler extends DefaultProcessDataChangeHandler {
    public static final String alias = "processProductDataChangeHandler";

    @PostConstruct
    public void postConstruct() {
        aliasMap.put(Business.PRODUCT_CHANGE, alias);
    }

    @Override
    protected void processAddOrUpdate(Message message) {
        JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findProductById(Long.parseLong(message.getId())));
        redisService.set(ProductKey.productKey, dataJSONObject.getString("id"), dataJSONObject.toJSONString());
        Map<String, Object> data = (Map<String, Object>) message.getData();
        data.put("productId", dataJSONObject.getString("id"));
    }

    @Override
    protected void processDelete(Message message) {
        redisService.delete(ProductKey.productKey, message.getId());
        Map<String, Object> data = (Map<String, Object>) message.getData();
        data.put("productId", message.getId());
    }
}
