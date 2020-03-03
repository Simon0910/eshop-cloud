package com.roncoo.eshop.datasync.biz;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
        message.setId(productId);
    }

    @Override
    protected void processDelete(Message message) {
        JSONObject dataJSONObject = findById(message.getId());
        String productId = dataJSONObject.getString("productId");
        redisService.delete(ProductKey.productPropertyKey, productId);
        message.setDataType(Business.PRODUCT_CHANGE);
        message.setId(productId);
    }

    private JSONObject findById(String id) {
        return  JSONObject.parseObject(eshopProductService.findProductPropertyById(Long.parseLong(id)));
    }
}
