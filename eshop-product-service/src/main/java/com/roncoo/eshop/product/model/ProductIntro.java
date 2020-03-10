package com.roncoo.eshop.product.model;

import com.roncoo.eshop.common.BaseEntity;
import lombok.Data;

@Data
public class ProductIntro extends BaseEntity {

    private String content;
    private Long productId;


}
