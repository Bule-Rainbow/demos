package com.jwt.demo.util.cache;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangwentao
 * @description 缓存测试
 * @createTime 2022年09月29日 14:21
 */
@RestController
@RequestMapping("/cache")
public class CacheTest {

    private static final Cache<String, String> CODE_CACHE = new Cache<>();

    @PostMapping("/test")
    public void testCache() {
        System.out.println(CODE_CACHE.get("111"));
        if (CODE_CACHE.get("111") == null) {
            CODE_CACHE.put("111", "aa1", 1, TimeUnit.MINUTES);
        }

    }

    public static void main(String[] args) throws InterruptedException {

        List<String> pathList = new ArrayList<>();
        pathList.add("/data/cmp/windows/files/569/569.txt");
        pathList.add("/data/cmp/windows/files/570/570.txt");

        StringBuilder msgBuilder = new StringBuilder("下载文件：");
        pathList.forEach(path -> {
            String name = new File(path).getName();
            msgBuilder.append(name).append(",");
        });
        String result = msgBuilder.toString();
        if (CollectionUtils.isNotEmpty(pathList)) {
            result = result.substring(0, result.length() - 1);
        }
        System.out.println(result);


    }
}
