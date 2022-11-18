package com.jwt.demo.util.other;

import com.jwt.demo.util.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: harbor工具类
 * @author: jiangwentao
 * @date: 2022/2/25 11:13
 */
public class HarborUtil {
    /**
     * haborV1.8版本可以通过下面的方式访问
     */
    public static final String HARBOR_WEB_LOGIN_URL = "/c/login";
    /**
     * 用于账号验证 替代/c/login方式
     */
    public static final String HARBOR_LOGIN_CHECK_URL = "/api/users/current";
    public static final String HARBOR_API_PROJECTS_URL = "/api/projects";
    public static final String HARBOR_API_REPOS_URL = "/api/repositories";

    /**
     * V2.0后可以通过这个接口查询API版本
     */
    public static final String HARBOR_API_VERSION_URL2 = "/api/version";
    /**
     * 用于账号验证 替代/c/login方式
     */
    public static final String HARBOR_LOGIN_CHECK_URL2 = "/api/?v/users/current";
    public static final String HARBOR_API_PROJECTS_URL2 = "/api/?v/projects";
    public static final String HARBOR_API_REPOS_URL2 = "/api/?v/projects/?project_name/repositories";
    public static final String HARBOR_API_ARTIFACTS_URL2 = "/api/?v/projects/?project_name/repositories/?repo_name/artifacts";
    public static final String HARBOR_API_IMAGE_HISTORY_URL2 = "/api/?v/projects/?project_name/repositories/?repo_name/artifacts/?sha/additions/build_history";

    /**
     * harbor仓库登录鉴权
     *
     * @param path     URL路径
     * @param username 用户名
     * @param password 密码
     * @return true：可访问，false：不可访问
     */
    private static boolean checklogin(String path, String username, String password) {
        try {
            //鉴于Harbor v2.0引入CSRF的限制 使用Web API（/c/login）方式来验证登录不再适用  统一换用users/current这个API
            Map<String, Object> headers = new HashMap<>(1);
            //设置Header认证参数
            String userMsg = username + ":" + password;
           // String authorization = "Basic " + org.apache.commons.codec.binary.Base64.encodeBase64String(userMsg.getBytes());
            String authorization = "Basic ";
            headers.put(HttpHeaders.AUTHORIZATION, authorization);
            //根据Harbor版本获取url
            String url = path + HARBOR_LOGIN_CHECK_URL;
            String versionResult = HttpUtil.doGet(path + HARBOR_API_VERSION_URL2);
            if (StringUtils.isNotBlank(versionResult) && versionResult.contains("version")) {
                url = path + HARBOR_LOGIN_CHECK_URL2.replace("?v", "v2.0");
            }
            //执行登录验证
            String loginResult = HttpUtil.doGet(url, headers, Collections.emptyMap());
            return !StringUtils.isBlank(loginResult) &&
                    !loginResult.contains("\"message\":\"UnAuthorize\"") &&
                    !loginResult.contains("\"code\":\"UNAUTHORIZED\"") &&
                    !loginResult.contains("\"code\":401");
        } catch (Exception e) {
            return false;
        }
    }
}
