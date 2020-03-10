package com.roncoo.eshop.datasync.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.keys.BrandKey;
import com.roncoo.eshop.common.rabbitmq.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzp on 2020/3/10.
 */
@Slf4j
@Component(value = "batchProcessBrandDataChangeHandler")
public class BatchProcessBrandDataChangeHandler extends DefaultProcessDataChangeHandler {
    public static final String alias = "batchProcessBrandDataChangeHandler";

    private List<Message> list = new ArrayList<Message>();

    @PostConstruct
    public void postConstruct() {
        aliasMap.put(Business.BATCH_BRAND_CHANGE, alias);
    }

    @Override
    protected void processAddOrUpdate(Message message) {
        synchronized (BatchProcessBrandDataChangeHandler.class) {
            list.add(message);
            int size = list.size();
            log.info("【将品牌数据放入内存list中】,list.size=" + size);
            if (size >= 2) {
                log.info("【将品牌数据内存list大小大于等于2，开始执行批量调用】");
                String ids = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    sb.append(list.get(i).getId());
                    if (i < size - 1) {
                        sb.append(",");
                    }
                }
                log.info("【品牌数据ids生成】ids=" + sb.toString());

                JSONArray dataJSONArray = JSONObject.parseArray(eshopProductService.findBrandByIds(sb.toString()));
                int brandSize = dataJSONArray.size();
                for (int i = 0; i < brandSize; i++) {
                    JSONObject dataJSONObject = dataJSONArray.getJSONObject(i);

                    redisService.set(BrandKey.brandKey, dataJSONObject.getString("id"), dataJSONObject.toJSONString());
                    log.info("【将品牌数据写入redis】brandId=" + dataJSONObject.getLong("id"));

                    // todo 已经和 DataChangeQueueReceiver  process方法 产生了无法解决的地步
                    // dimDataChangeMessageSet.add("{\"dim_type\": \"brand\", \"id\": " + dataJSONObject.getLong("id") + "}");
                    // log.info("【将品牌数据写入内存去重set中】brandId=" + dataJSONObject.getLong("id"));
                }

                list.clear();
            }
        }
    }

    @Override
    protected void processDelete(Message message) {
        redisService.delete(BrandKey.brandKey, message.getId());
    }
}
