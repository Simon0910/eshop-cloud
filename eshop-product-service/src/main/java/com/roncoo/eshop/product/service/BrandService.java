package com.roncoo.eshop.product.service;

import com.roncoo.eshop.product.model.Brand;

import java.util.List;

public interface BrandService {

    void add(Brand brand);

    void update(Brand brand);

    void delete(Long id);

    Brand findById(Long id);

    List<Brand> findByIds(String ids);
}
