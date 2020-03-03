package com.roncoo.eshop.common.keys;

import com.roncoo.eshop.common.redis.keys.BasePrefix;

public class CategoryKey extends BasePrefix {

    public CategoryKey(String prefix) {
        super(prefix);
    }

    public CategoryKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static CategoryKey categoryKey = new CategoryKey("category:");
    public static CategoryKey categoryExpireKey = new CategoryKey(60 * 60 * 24 * 90, "category:");
}
