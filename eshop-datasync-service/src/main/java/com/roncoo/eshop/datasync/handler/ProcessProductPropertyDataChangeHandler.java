package com.roncoo.eshop.datasync.handler;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component(value = "processProductPropertyDataChangeHandler")
public class ProcessProductPropertyDataChangeHandler extends DefaultProcessDataChangeHandler {
    public static final String alias = "processProductPropertyDataChangeHandler";

    @PostConstruct
    public void postConstruct() {
        aliasMap.put(Business.PRODUCT_PROPERTY_CHANGE, alias);
    }

    @Override
    protected void processAddOrUpdate(Message message) {
        JSONObject dataJSONObject = findById(message.getId());
        String productId = dataJSONObject.getString("productId");
        redisService.set(ProductKey.productPropertyKey, productId, dataJSONObject.toJSONString());
        message.setDataType(Business.PRODUCT_CHANGE);
    }

    @Override
    protected void processDelete(Message message) {
        JSONObject dataJSONObject = findById(message.getId());
        if (dataJSONObject == null) {
            Map<String, Object> data = (Map<String, Object>) message.getData();
            String productId = String.valueOf(data.get("productId"));
            redisService.delete(ProductKey.productPropertyKey, productId);
        }
        message.setDataType(Business.PRODUCT_CHANGE);
    }

    private JSONObject findById(String id) {
        return  JSONObject.parseObject(eshopProductService.findProductPropertyById(Long.parseLong(id)));
    }
}
