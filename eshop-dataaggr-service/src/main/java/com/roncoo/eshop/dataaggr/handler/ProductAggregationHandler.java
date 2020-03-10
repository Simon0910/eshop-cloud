package com.roncoo.eshop.dataaggr.handler;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "productAggregationHandler")
public class ProductAggregationHandler extends DefaultAggregationHandler {

    @Override
    protected void processAddOrUpdate(Message message) {
        Map<String, Object> data = (Map<String, Object>) message.getData();
        String productId = String.valueOf(data.get("productId"));

        JSONObject productJSONObject = redisService.getJSONObject(ProductKey.productKey, productId);
        if (productJSONObject == null) {
            productJSONObject = new JSONObject();
        }
        String productPropertyJSON = redisService.get(ProductKey.productPropertyKey, productId);
        if (productPropertyJSON != null) {
            productJSONObject.put(ProductKey.productPropertyKey.getPrefix(), productPropertyJSON);
        }
        String productSpecificationJSON = redisService.get(ProductKey.productSpecificationKey, productId);
        if (productSpecificationJSON != null) {
            productJSONObject.put(ProductKey.productSpecificationKey.getPrefix(), productSpecificationJSON);
        }
        int size = productJSONObject.size();
        if (size > 1) {
            redisService.set(ProductKey.aggregationKey, productId, productJSONObject.toJSONString());
        }
    }
}
