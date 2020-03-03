package com.roncoo.eshop.common.keys;

import com.roncoo.eshop.common.redis.keys.BasePrefix;

public class ProductKey extends BasePrefix {

    public ProductKey(String prefix) {
        super(prefix);
    }

    public ProductKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static ProductKey aggregationKey = new ProductKey( "aggregationKey:");
    public static ProductKey productKey = new ProductKey("product:");
    public static ProductKey productIntroKey = new ProductKey("productIntro:");
    public static ProductKey productSpecificationKey = new ProductKey("productSpecification:");
    public static ProductKey productPropertyKey = new ProductKey("productProperty:");
}
