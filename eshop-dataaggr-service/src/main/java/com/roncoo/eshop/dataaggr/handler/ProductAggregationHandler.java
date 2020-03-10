package com.roncoo.eshop.dataaggr.handler;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "productAggregationHandler")
public class ProductAggregationHandler extends DefaultAggregationHandler {

    @Override
    protected void processAddOrUpdate(Message message) {
        Map<String, Object> data = (Map<String, Object>) message.getData();
        String productId = String.valueOf(data.get("productId"));

        List<String> list = redisService.mget(
                ProductKey.productKey.getKeyPrefix() + productId,
                ProductKey.productPropertyKey.getKeyPrefix() + productId, 
                ProductKey.productSpecificationKey.getKeyPrefix() + productId);
        String productJSON = list.get(0);
        JSONObject productJSONObject = null;
        if (StringUtils.isBlank(productJSON)) {
            productJSONObject = new JSONObject();
        } else {
            productJSONObject = JSONObject.parseObject(productJSON);
        }

        String productPropertyJSON = list.get(1);
        if (productPropertyJSON != null) {
            productJSONObject.put(ProductKey.productPropertyKey.getPrefix(), productPropertyJSON);
        }
        String productSpecificationJSON = list.get(2);
        if (productSpecificationJSON != null) {
            productJSONObject.put(ProductKey.productSpecificationKey.getPrefix(), productSpecificationJSON);
        }
        int size = productJSONObject.size();
        if (size > 1) {
            redisService.set(ProductKey.aggregationKey, productId, productJSONObject.toJSONString());
        }
    }

    // @Override
    // protected void processAddOrUpdate(Message message) {
    //     Map<String, Object> data = (Map<String, Object>) message.getData();
    //     String productId = String.valueOf(data.get("productId"));
    //
    //     JSONObject productJSONObject = redisService.getJSONObject(ProductKey.productKey, productId);
    //     if (productJSONObject == null) {
    //         productJSONObject = new JSONObject();
    //     }
    //     String productPropertyJSON = redisService.get(ProductKey.productPropertyKey, productId);
    //     if (productPropertyJSON != null) {
    //         productJSONObject.put(ProductKey.productPropertyKey.getPrefix(), productPropertyJSON);
    //     }
    //     String productSpecificationJSON = redisService.get(ProductKey.productSpecificationKey, productId);
    //     if (productSpecificationJSON != null) {
    //         productJSONObject.put(ProductKey.productSpecificationKey.getPrefix(), productSpecificationJSON);
    //     }
    //     int size = productJSONObject.size();
    //     if (size > 1) {
    //         redisService.set(ProductKey.aggregationKey, productId, productJSONObject.toJSONString());
    //     }
    // }
}
