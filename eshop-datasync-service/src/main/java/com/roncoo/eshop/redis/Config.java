package com.roncoo.eshop.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Slf4j
@Configuration
public class Config {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(2);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(10 * 1000);
        return new JedisPool(config, "127.0.0.1", 6379);
    }
}
