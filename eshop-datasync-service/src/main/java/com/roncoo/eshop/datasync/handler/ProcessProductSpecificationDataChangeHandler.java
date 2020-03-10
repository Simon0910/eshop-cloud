package com.roncoo.eshop.datasync.handler;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component(value = "processProductSpecificationDataChangeHandler")
public class ProcessProductSpecificationDataChangeHandler extends DefaultProcessDataChangeHandler {
    public static final String alias = "processProductSpecificationDataChangeHandler";

    @PostConstruct
    public void postConstruct() {
        aliasMap.put(Business.PRODUCT_SPECIFICATION_CHANGE, alias);
    }

    @Override
    protected void processAddOrUpdate(Message message) {
        JSONObject dataJSONObject = findById(message.getId());
        String productId = dataJSONObject.getString("productId");
        redisService.set(ProductKey.productSpecificationKey, productId, dataJSONObject.toJSONString());
        message.setDataType(Business.PRODUCT_CHANGE);
    }

    @Override
    protected void processDelete(Message message) {
        JSONObject dataJSONObject = findById(message.getId());
        if (dataJSONObject == null) {
            Map<String, Object> data = (Map<String, Object>) message.getData();
            String productId = String.valueOf(data.get("productId")); // todo 第一次删除不掉, 就没有机会删除了
            redisService.delete(ProductKey.productSpecificationKey, productId);
        }
        message.setDataType(Business.PRODUCT_CHANGE);
    }

    private JSONObject findById(String id) {
        String productSpecificationJsonStr = eshopProductService.findProductSpecificationById(Long.parseLong(id));
        return  JSONObject.parseObject(productSpecificationJsonStr);
    }
}
