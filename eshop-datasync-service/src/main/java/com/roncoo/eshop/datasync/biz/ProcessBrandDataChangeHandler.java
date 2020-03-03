package com.roncoo.eshop.datasync.biz;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.BrandKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component(value = "processBrandDataChangeHandler")
public class ProcessBrandDataChangeHandler extends DefaultProcessDataChangeHandler {
    public static final String alias = "processBrandDataChangeHandler";

    @PostConstruct
    public void postConstruct() {
        aliasMap.put(Business.BRAND_CHANGE, alias);
    }

    @Override
    protected void processAddOrUpdate(Message message) {
        JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findBrandById(Long.parseLong(message.getId())));
        redisService.set(BrandKey.brandKey, dataJSONObject.getString("id"), dataJSONObject.toJSONString());
    }

    @Override
    protected void processDelete(Message message) {
        redisService.delete(BrandKey.brandKey, message.getId());
    }
}
