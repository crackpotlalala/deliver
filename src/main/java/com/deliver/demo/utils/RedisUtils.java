package com.deliver.demo.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 */
@Component
public class RedisUtils {
    private static RedisTemplate redisTemplate;

    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static Boolean hasKey(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey;
    }

    public static Set<String> keys(String keyPattern) {
        return redisTemplate.keys(keyPattern);
    }

    public static void setHKeyValueWithNoExpire(String key, String hKey, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hKey)) {
            return;
        }
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    public static String getHKeyValueWithNoExpire(String key, String hKey) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hKey)) {
            return null;
        }
        return (String) redisTemplate.opsForHash().get(key, hKey);
    }

    public static boolean hasHkey(String key, String hKey) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hKey)) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, hKey);
    }

    public static void setStringValueWithExpire(String key, String value, Long time) {
        redisTemplate.boundValueOps(key).set(value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public static String getStringValue(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return (String) redisTemplate.boundValueOps(key).get();
    }

    public static void setStringValue(String key, String value) {
        redisTemplate.boundValueOps(key).set(value);
    }

    public static String getStringValueWithExpire(String key, Long time) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String value = (String) redisTemplate.boundValueOps(key).get();
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
        return value;
    }

    public static void convertAndSend(String topic, String message) {
        redisTemplate.convertAndSend(topic, message);
    }

    public static void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public static void putValueInSet(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        redisTemplate.opsForSet().add(key, value);
    }

    public static Boolean isValueInSet(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return false;
        }
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 根据前缀批量删除
     * 注意：keys函数效率相对较低，需谨慎使用，避免慢查询导致整个redis效率低下。
     * prefix应该尽量具体！！！
     *
     * @param prefix
     */
    public static void batchDel(String prefix) {
        Set<String> set = redisTemplate.keys(prefix + "*");
        redisTemplate.delete(set);
    }

    public void deleteHKey(String key, String... hKey) {
        redisTemplate.opsForHash().delete(key, hKey);
    }


}

