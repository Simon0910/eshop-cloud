package com.roncoo.eshop.datasync.biz;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.BrandKey;
import com.roncoo.eshop.common.keys.CategoryKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component(value = "processCategoryDataChangeHandler")
public class ProcessCategoryDataChangeHandler extends DefaultProcessDataChangeHandler {
    public static final String alias = "processCategoryDataChangeHandler";

    @PostConstruct
    public void postConstruct() {
        aliasMap.put(Business.CATEGORY_CHANGE, alias);
    }

    @Override
    protected void processAddOrUpdate(Message message) {
        JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findCategoryById(Long.parseLong(message.getId())));
        redisService.set(CategoryKey.categoryKey, dataJSONObject.getString("id"), dataJSONObject.toJSONString());
    }

    @Override
    protected void processDelete(Message message) {
        redisService.delete(CategoryKey.categoryKey, message.getId());
    }
}
