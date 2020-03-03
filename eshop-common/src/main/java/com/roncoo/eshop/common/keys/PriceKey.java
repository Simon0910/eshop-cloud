package com.roncoo.eshop.common.keys;

import com.roncoo.eshop.common.redis.keys.BasePrefix;

public class PriceKey extends BasePrefix {

    public PriceKey(String prefix) {
        super(prefix);
    }

    public PriceKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static PriceKey priceKey = new PriceKey("price:");
    public static PriceKey priceExpireKey = new PriceKey(60 * 60 * 24 * 90, "price:");
}
