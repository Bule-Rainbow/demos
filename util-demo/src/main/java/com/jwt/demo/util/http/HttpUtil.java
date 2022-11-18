package com.jwt.demo.util.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpUtil {

    // 定义请求客户端
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * get 请求
     *
     * @param url 请求URL路径
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doGet(String url) throws IOException {
        return doGet(url, Collections.emptyMap());
    }

    /**
     * get 请求
     *
     * @param url   请求URL路径
     * @param query 携带参数参数
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doGet(String url, Map<String, Object> query) throws IOException {
        return doGet(url, Collections.emptyMap(), query);
    }

    /**
     * post 请求  携带文件上传
     *
     * @param url  请求URL路径
     * @param file 上传文件
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, File file) throws IOException {
        return doPost(url, Collections.emptyMap(), Collections.emptyMap(), file);
    }

    /**
     * post 请求
     *
     * @param url       请求URL路径
     * @param header    请求头
     * @param parameter 参数
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, Object> header, Map<String, Object> parameter) throws IOException {
        return doPost(url, header, parameter, null);
    }

    /**
     * post 请求
     *
     * @param url       请求URL路径
     * @param parameter 参数
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, Object> parameter) throws IOException {
        return doPost(url, Collections.emptyMap(), parameter, null);
    }

    /**
     * get 请求
     *
     * @param url    请求URL路径
     * @param header 请求头参数
     * @param query  参数
     * @return 执行结果
     */
    public static String doGet(String url, Map<String, Object> header, Map<String, Object> query) throws IOException {
        log.debug("okhttp请求参数: url: {}, header: {}. query: {}", url, header, query);

        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(url).build();

        // 创建一个 HttpUrl.Builder
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();

        // 装载请求头参数
        Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
        headerIterator.forEachRemaining(e -> headerBuilder.add(e.getKey(), (String) e.getValue()));

        // 装载请求的参数
        Iterator<Map.Entry<String, Object>> queryIterator = query.entrySet().iterator();
        queryIterator.forEachRemaining(e -> urlBuilder.addQueryParameter(e.getKey(), (String) e.getValue()));

        // 设置自定义的 builder
        builder.url(urlBuilder.build()).headers(headerBuilder.build());

        try (Response execute = client.newCall(builder.build()).execute()) {
            String resp = Objects.requireNonNull(execute.body()).string();
            log.debug("okhttp请求返回数据: {}", resp);
            return resp;
        }
    }


    /**
     * post 请求， 请求参数 并且 携带文件上传
     *
     * @param url       请求URL路径
     * @param header    请求头
     * @param parameter 请求参数
     * @param file      上传文件
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, Object> header, Map<String, Object> parameter, File file) throws IOException {
        log.debug("okhttp请求信息: url: {}, header: {}. parameter: {}", url, header, parameter);
        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(url).build();

        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();

        // 装载请求头参数
        Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
        headerIterator.forEachRemaining(e -> headerBuilder.add(e.getKey(), (String) e.getValue()));

        MultipartBody.Builder requestBuilder = new MultipartBody.Builder();

        // 状态请求参数
        Iterator<Map.Entry<String, Object>> queryIterator = parameter.entrySet().iterator();
        queryIterator.forEachRemaining(e -> requestBuilder.addFormDataPart(e.getKey(), (String) e.getValue()));

        if (null != file) {
            // application/octet-stream
            requestBuilder.addFormDataPart("uploadFiles", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")));
        }

        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(requestBuilder.build());

        // 然后再 build 一下
        try (Response execute = client.newCall(builder.build()).execute()) {
            String resp = Objects.requireNonNull(execute.body()).string();
            log.debug("okhttp请求返回数据: {}", resp);
            return resp;
        }
    }

    /**
     * post 请求， 请求参数 并且 携带文件上传二进制流
     *
     * @param url       请求URL路径
     * @param header    请求头
     * @param parameter 请求参数
     * @param fileName  文件名
     * @param fileByte  文件的二进制流
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, Object> header, Map<String, Object> parameter, String fileName, byte[] fileByte) throws IOException {
        log.debug("okhttp请求信息: url: {}, header: {}, parameter: {}", url, header, parameter);
        // 创建一个请求 Builder
        Request.Builder builder = new Request.Builder();
        // 创建一个 request
        Request request = builder.url(url).build();

        // 创建一个 Headers.Builder
        Headers.Builder headerBuilder = request.headers().newBuilder();

        // 装载请求头参数
        Iterator<Map.Entry<String, Object>> headerIterator = header.entrySet().iterator();
        headerIterator.forEachRemaining(e -> headerBuilder.add(e.getKey(), (String) e.getValue()));

        MultipartBody.Builder requestBuilder = new MultipartBody.Builder();

        // 状态请求参数
        Iterator<Map.Entry<String, Object>> queryIterator = parameter.entrySet().iterator();
        queryIterator.forEachRemaining(e -> requestBuilder.addFormDataPart(e.getKey(), (String) e.getValue()));

        if (fileByte.length > 0) {
            requestBuilder.addFormDataPart("uploadFiles", fileName, RequestBody.create(fileByte, MediaType.parse("application/octet-stream")));
        }

        // 设置自定义的 builder
        builder.headers(headerBuilder.build()).post(requestBuilder.build());

        try (Response execute = client.newCall(builder.build()).execute()) {
            String resp = Objects.requireNonNull(execute.body()).string();
            log.debug("okhttp请求返回数据: {}", resp);
            return resp;
        }
    }

    /**
     * 使用org.apache.commons.httpclient.HttpClient发送post请求
     *
     * @param url      请求URL路径
     * @param username 用户名
     * @param password 密码
     * @return 请求结果，true表示成功（返回码200）
     * @throws IOException IO异常
     */
    public static Boolean sendPostWithParams(String url, String username, String password) throws IOException {
        HttpClient client = new HttpClient();
        client.setConnectionTimeout(3000);
        PostMethod postMethod = new PostMethod(url);
        postMethod.setParameter("username", username);
        postMethod.setParameter("password", password);
        System.out.println(client.executeMethod(postMethod));
        return client.executeMethod(postMethod) == 200;
    }

    /**
     * post请求 带上传文件和参数
     *
     * @param url         路径
     * @param file        文件
     * @param paramerList 参数
     * @throws Exception 异常
     */
    public static String sendPostwithFileAndParams(String url, File file, Map<String, String> paramerList) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody);

        for (Map.Entry<String, String> entry : paramerList.entrySet()) {
            requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder().url(url).post(requestBodyBuilder.build()).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 使用org.apache.commons.httpclient.HttpClient发送post请求
     *
     * @param url 请求URL路径
     * @return 请求结果，true表示成功（返回码200）
     * @throws IOException IO异常
     */
    public static String sendPostWithParams(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        if (!headers.isEmpty()) {
            Iterator<Map.Entry<String, Object>> headerIterator = headers.entrySet().iterator();
            headerIterator.forEachRemaining(e -> postMethod.setRequestHeader(e.getKey(), (String) e.getValue()));
        }

        if (!params.isEmpty()) {
            Iterator<Map.Entry<String, Object>> paramsIterator = params.entrySet().iterator();
            paramsIterator.forEachRemaining(e -> postMethod.setParameter(e.getKey(), (String) e.getValue()));
        }

        if (client.executeMethod(postMethod) != 200) {
            log.error(postMethod.getResponseBodyAsString());
            //throw new RuntimeException("用户名或密码错误，登录失败");
        }
        return postMethod.getResponseBodyAsString();
    }

    /**
     * https请求，忽略证书
     *
     * @param url         路径
     * @param paramerList 参数
     * @throws Exception 异常
     */
    public static String sendHttpsPost(String url, Map<String, String> paramerList) throws Exception {
        OkHttpClient client = getHttpsClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), JSONObject.toJSONString(paramerList));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 获取https请求client  并跳过证书验证
     *
     * @return https client
     * @throws Exception 异常
     */
    private static OkHttpClient getHttpsClient() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        clientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true);
        return clientBuilder.build();
    }
}
