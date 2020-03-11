package com.roncoo.eshop.common.keys;

import com.roncoo.eshop.common.redis.keys.BasePrefix;

/**
 * @author lzp on 2020/3/11.
 */
public class InventoryKey extends BasePrefix {
    public InventoryKey(String prefix) {
        super(prefix);
    }

    public InventoryKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static InventoryKey inventoryKey = new InventoryKey("inventory:");
    public static InventoryKey inventoryExpireKey = new InventoryKey(60 * 60 * 24 * 90, "inventory:");
}
