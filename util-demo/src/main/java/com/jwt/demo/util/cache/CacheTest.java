package com.jwt.demo.util.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangwentao
 * @description 缓存测试
 * @createTime 2022年09月29日 14:21
 */
public class CacheTest {

    private static final Cache<String, String> CODE_CACHE = new Cache<>();

    public static void main(String[] args) {
        String str = "jiangwentao";
        String code = "123456";
        CODE_CACHE.put(str + "#hci", code, 60, TimeUnit.MINUTES);
        System.out.println(CODE_CACHE.get(str + "#hci"));

        CODE_CACHE.put("123" + "#hci", "code", 60, TimeUnit.MINUTES);
        System.out.println(CODE_CACHE.get("123" + "#hci"));
    }
}
