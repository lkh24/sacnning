package kh.cn.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/7 10:03 PM
 */
@Component
public class CacheUtil {
    @Autowired
    private CacheManager cacheManager;

    /**
     * 添加缓存
     *
     * @param data 数据
     */
    public void put(Map<String, Object> data) {
        Cache cache = cacheManager.getCache("caffeineCache");
        if (cache != null) {
            // 将数据保存到缓存中
            cache.put(data, Duration.ofSeconds(60));
        }
    }

    /**
     * 从缓存中获取指定键的值
     *
     * @param key 缓存键
     * @return 缓存值，如果不存在则返回 null
     */
    public Object get(String key) {
        Cache cache = cacheManager.getCache("caffeineCache");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                return valueWrapper.get();
            }
        }
        return null;
    }

    /**
     * 从缓存中删除指定键的值
     *
     * @param key 缓存键
     */
    public void evict(String key) {
        Cache cache = cacheManager.getCache("caffeineCache");
        if (cache != null) {
            cache.evict(key);
        }
    }
}
