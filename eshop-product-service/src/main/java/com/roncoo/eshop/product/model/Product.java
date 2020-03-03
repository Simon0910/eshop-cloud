package com.roncoo.eshop.product.model;

import com.roncoo.eshop.common.BaseEntity;
import lombok.Data;

@Data
public class Product extends BaseEntity {

    private String name;
    private Long categoryId;
    private Long brandId;

}
