package com.roncoo.eshop.dataaggr.biz;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

@Component(value = "productAggregationHandler")
public class ProductAggregationHandler extends DefaultAggregationHandler {

    @Override
    protected void processAddOrUpdate(Message message) {
        JSONObject productJSONObject = redisService.getJSONObject(ProductKey.productKey, message.getId());
        if (productJSONObject == null) {
            productJSONObject = new JSONObject();
        }
        String productPropertyJSON = redisService.get(ProductKey.productPropertyKey, message.getId());
        if (productPropertyJSON != null) {
            Object o = productJSONObject.get(ProductKey.productPropertyKey.getPrefix());
        }
        String productSpecificationJSON = redisService.get(ProductKey.productSpecificationKey, message.getId());
        if (productSpecificationJSON != null) {
            productJSONObject.put(ProductKey.productSpecificationKey.getPrefix(), productSpecificationJSON);
        }
        redisService.set(ProductKey.aggregationKey, message.getId(), productJSONObject);
    }
}
