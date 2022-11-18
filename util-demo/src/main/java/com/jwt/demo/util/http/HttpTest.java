package com.jwt.demo.util.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangwentao
 * @description http请求测试
 * @createTime 2022年09月29日 16:06
 */
public class HttpTest {
    public static void main(String[] args) throws Exception {

        //执行http post请求，带file文件上传和参数
        File file = new File("F:\\script\\haproxy.ini");
        String url = "http://10.31.1.140:7080/trans";
        Map<String, String> params1 = new HashMap<>(1);
        params1.put("trans_type", "haproxy");
        String resultStr1 = HttpUtil.sendPostwithFileAndParams(url, file, params1);
        System.out.println(resultStr1);

        //执行https post请求，带参数
        url = "https://10.8.11.12:9004/hci/api/sys/login";
        Map<String, String> params2 = new HashMap<>(1);
        params2.put("username","jiangwentao");
        params2.put("password","741be639c6d0d28eb43cac15f9d567fb0ff88e3e132caeb12a77ea931a594dc13f1caa4384c7ccec6eda6b2500f91de9cbf6cda25573e27a8684ec0d9af9dd34");
        String resultStr2 =HttpUtil.sendHttpsPost(url, params2);
        System.out.println(resultStr2);
    }
}
