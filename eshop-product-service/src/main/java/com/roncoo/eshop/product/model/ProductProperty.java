package com.roncoo.eshop.product.model;

import com.roncoo.eshop.common.BaseEntity;
import lombok.Data;

@Data
public class ProductProperty extends BaseEntity {

    private String name;
    private String value;
    private Long productId;

}
