package com.roncoo.eshop.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.keys.InventoryKey;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import com.roncoo.eshop.redis.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    RedisService redisService;

    @Override
    public void add(ProductInventory productInventory) {
        productInventoryMapper.add(productInventory);
        redisService.set(ProductKey.productKey, String.valueOf(productInventory.getId()), productInventory);
    }

    @Override
    public void update(ProductInventory productInventory) {
        productInventoryMapper.update(productInventory);
        redisService.set(ProductKey.productKey, String.valueOf(productInventory.getId()), productInventory);
    }

    @Override
    public void delete(Long id) {
        ProductInventory productInventory = findById(id);
        productInventoryMapper.delete(productInventory.getId());
        redisService.delete(ProductKey.productKey, String.valueOf(productInventory.getId()));
    }

    @Override
    public ProductInventory findById(Long id) {
        return productInventoryMapper.findById(id);
    }

    @Override
    public ProductInventory findByProductId(Long productId) {
        String dataJSON = redisService.get(InventoryKey.inventoryKey, String.valueOf(productId));
        if (StringUtils.isNotBlank(dataJSON)) {
            JSONObject dataJSONObject = JSONObject.parseObject(dataJSON);
            dataJSONObject.put("id", "-1");
            return JSONObject.parseObject(dataJSONObject.toJSONString(), ProductInventory.class);
        } else {
            return productInventoryMapper.findByProductId(productId);
        }
    }

}
