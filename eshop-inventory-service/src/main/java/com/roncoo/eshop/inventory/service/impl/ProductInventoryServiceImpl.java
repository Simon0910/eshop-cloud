package com.roncoo.eshop.inventory.service.impl;

import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import com.roncoo.eshop.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    RedisService redisService;

    public void add(ProductInventory productInventory) {
        productInventoryMapper.add(productInventory);
        redisService.set(ProductKey.productKey, String.valueOf(productInventory.getId()), productInventory);
    }

    public void update(ProductInventory productInventory) {
        productInventoryMapper.update(productInventory);
        redisService.set(ProductKey.productKey, String.valueOf(productInventory.getId()), productInventory);
    }

    public void delete(Long id) {
        ProductInventory productInventory = findById(id);
        productInventoryMapper.delete(productInventory.getId());
        redisService.delete(ProductKey.productKey, String.valueOf(productInventory.getId()));
    }

    public ProductInventory findById(Long id) {
        return productInventoryMapper.findById(id);
    }

}
