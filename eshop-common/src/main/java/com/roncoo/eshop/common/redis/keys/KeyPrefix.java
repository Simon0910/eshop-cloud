package com.roncoo.eshop.common.redis.keys;

public interface KeyPrefix {

    int expireSeconds();

    String getKeyPrefix();

    String generateKey(String id);
}
