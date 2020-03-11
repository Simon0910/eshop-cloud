package com.roncoo.eshop.price.service;

import com.roncoo.eshop.price.model.ProductPrice;

public interface ProductPriceService {

    void add(ProductPrice productPrice);

    void update(ProductPrice productPrice);

    void delete(Long id);

    ProductPrice findById(Long id);

    ProductPrice findByProductId(Long productId);

}
