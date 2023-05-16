package kh.cn.util.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


/**
 * @author ：Lkh
 * @date ：Created in 2023/5/7 10:13 PM
 */
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public Cache caffeineCache() {
        return new CaffeineCache(
                "caffeineCache",
                Caffeine.newBuilder()
//                        最大容量
                        .maximumSize(1000)
//                                  过期时间
                        .expireAfterWrite(60, TimeUnit.SECONDS)
                        .build()
        );
    }

}

