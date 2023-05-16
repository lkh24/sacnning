package kh.cn.domin;

import lombok.Data;

/**
 * @author ：Lkh
 * @date ：Created in 2023/5/12 10:05 PM
 */
@Data
public class User {
    /**
     * 用户的userId
     */
    private String userId;

    /**
     * 缓存字符串
     */
    private String sjToken;
}
