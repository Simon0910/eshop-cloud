package com.roncoo.eshop.inventory.service;

import com.roncoo.eshop.inventory.model.ProductInventory;

public interface ProductInventoryService {

    void add(ProductInventory productInventory);

    void update(ProductInventory productInventory);

    void delete(Long id);

    ProductInventory findById(Long id);

    ProductInventory findByProductId(Long productId);

}
