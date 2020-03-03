package com.roncoo.eshop.product.model;

import com.roncoo.eshop.common.BaseEntity;
import lombok.Data;

@Data
public class Category extends BaseEntity {

    private String name;
    private String description;

}
