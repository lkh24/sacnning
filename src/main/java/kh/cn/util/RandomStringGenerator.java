package kh.cn.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/8 11:37 AM
 * 启动定时器，将随机字符串存储到缓存中
 */
@Component
public class RandomStringGenerator {

    /**
     * 随机字符串长度
     */
    private static final int RANDOM_STRING_LENGTH = 10;
    @Autowired
    private CacheUtil cacheUtil;


    /**
     * 定时生成字符串，并且推送到cache中
     */
    @Scheduled(cron = "0 * * * * *") // 每分钟执行一次
    public void saveRandomStringToCache() {
        // 生成随机字符串
        String randomString = generateRandomString(RANDOM_STRING_LENGTH);

        Map data =  new HashMap<String,Object>();
        data.put("sj", randomString);
        // 将数据保存到缓存中，有效期为 60 秒
        cacheUtil.put(data);
    }


    /**
     * 将时间戳作为种子，当做随机的生成逻辑
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    private String generateRandomString(int length) {
        // 获取时间
        long nowTime = System.currentTimeMillis();
        String timeStr = Long.toString(nowTime);
        byte[] timeBytes = timeStr.getBytes();

//        创建指定字符长度容器
        byte[] randomBytes = new byte[length];

//        随机生成字符串逻辑
        new SecureRandom(timeBytes).nextBytes(randomBytes);

        return new BigInteger(1,randomBytes).toString(16).toUpperCase().substring(0,length);
    }
}
