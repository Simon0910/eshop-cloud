package com.roncoo.eshop.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.redis.keys.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;


    /**
     * 获取当个对象
     */
    public String get(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(prefix.getKeyPrefix() + key);
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            return str;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 获取当个对象
     */
    public JSONObject getJSONObject(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(prefix.getKeyPrefix() + key);
            return JSONObject.parseObject(str);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 获取当个对象
     */
    public List<String> mget(String... keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> list = jedis.mget(keys);
            return list;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(prefix.getKeyPrefix() + key, str);
            } else {
                jedis.setex(prefix.getKeyPrefix() + key, seconds, str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            return jedis.exists(prefix.getKeyPrefix() + key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            return jedis.incr(prefix.getKeyPrefix() + key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            return jedis.decr(prefix.getKeyPrefix() + key);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除一个key
     *
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            return jedis.del(prefix.getKeyPrefix() + key) > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * @param prefix
     * @param key
     * @return
     */
    public boolean deleteBatch(KeyPrefix prefix, String key) {
        if (prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix, key);
        if (keys == null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> scanKeys(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<String>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match(prefix.getKeyPrefix() + key + "*");
            sp.count(100);
            do {
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if (result != null && result.size() > 0) {
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getCursor();// todo 未测试
            } while (!cursor.equals("0"));
            return keys;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private static final String ratelimiting = "local times = redis.call('incr',KEYS[1]); if times > tonumber(KEYS[2]) then return 0; end if times == 1 then redis.call('expire',KEYS[3],KEYS[4]); end return 1;";

    public Object ratelimit(KeyPrefix prefix, String key, int maxCount) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String pk = String.valueOf(prefix.getKeyPrefix() + key);
            return jedis.eval(ratelimiting, 4,
                    pk, String.valueOf(maxCount),
                    pk, String.valueOf(prefix.expireSeconds()));

        } finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSONObject.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSONObject.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}
