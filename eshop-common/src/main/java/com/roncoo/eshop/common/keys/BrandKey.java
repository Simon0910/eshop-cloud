package com.roncoo.eshop.common.keys;

import com.roncoo.eshop.common.redis.keys.BasePrefix;

public class BrandKey extends BasePrefix {

    public BrandKey(String prefix) {
        super(prefix);
    }

    public BrandKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static BrandKey brandKey = new BrandKey("brand:");
    public static BrandKey brandExpireKey = new BrandKey(60 * 60 * 24 * 90, "brand:");
}
