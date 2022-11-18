package com.sinnet.guacamole.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sinnet.guacamole.demo.util.HttpUtil;
import com.sinnet.guacamole.demo.vo.ConnectionAddReq;
import com.sinnet.guacamole.demo.vo.ConnectionEditReq;
import com.sinnet.guacamole.demo.vo.ConnectionListResp;
import com.sinnet.guacamole.demo.vo.GuacaLoginReq;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwentao
 * @description Guac test
 * @createTime 2022年10月24日 11:10
 */
@Slf4j
@RestController
@RequestMapping("/guacamole")
public class GuacamoleController {

    @Value("${guacamole.baseUrl}")
    private String baseUrl;

    @Value("${guacamole.serverIp}")
    private String serverIp;

    @Value("${guacamole.serverPort}")
    private String serverPort;


    @ApiOperation("测试文件上传")
    @PostMapping("/file/upload")
    public void getTunnelIdBy(@RequestParam("files") List<MultipartFile> files) {
        files.forEach(file -> {
            String originalFilename = file.getOriginalFilename();
            try {
                InputStream inputStream = file.getInputStream();
                BufferedInputStream in = new BufferedInputStream(inputStream);
                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                byte[] temp = new byte[1024];
                int size;
                while ((size = in.read(temp)) != -1) {
                    out.write(temp, 0, size);
                }
                in.close();
                byte[] content = out.toByteArray();

                String filePath = "/tmp";
                Session session;
                Channel channel = null;

                JSch jSch = new JSch();
                session = jSch.getSession("root", "10.31.1.65", 22);
                //如果服务器连接不上，则抛出异常
                if (session == null) {
                    throw new Exception("session is null");
                }

                //设置登陆主机的密码
                session.setPassword("123456");
                //设置第一次登陆的时候提示，可选值：(ask | yes | no)
                session.setConfig("userauth.gssapi-with-mic", "no");
                session.setConfig("StrictHostKeyChecking", "no");
                //设置登陆超时时间
                session.connect(30000);
                OutputStream outstream = null;
                try {
                    //创建sftp通信通道
                    channel = session.openChannel("sftp");
                    channel.connect(1000);
                    ChannelSftp sftp = (ChannelSftp) channel;

                    //进入服务器指定的文件夹
                    sftp.cd(filePath);

                    //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换一下流就可以了
                    outstream = sftp.put(originalFilename);
                    outstream.write(content);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //关流操作
                    if (outstream != null) {
                        outstream.flush();
                        outstream.close();
                    }
                    session.disconnect();
                    if (channel != null) {
                        channel.disconnect();
                    }
                    System.out.println("上传成功!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @ApiOperation("登录")
    @PostMapping("/login")
    public JSONObject login(@RequestBody GuacaLoginReq req) {
        String loginUrl = String.format("%s/tokens", baseUrl);
        String s = null;
        try {
            Map<String, Object> parameter = new HashMap<>(2);
            parameter.put("username", req.getUsername());
            parameter.put("password", req.getPassword());
            s = HttpUtil.sendPostWithParams(loginUrl, parameter, Collections.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(s);
    }

    @ApiOperation("通过token获取信息")
    @PostMapping("/token")
    public JSONObject token(@RequestBody GuacaLoginReq req) {
        String tokenUrl = String.format("http://%s/guacamole/api/tokens", baseUrl);
        String s = null;
        try {
            Map<String, Object> parameter = new HashMap<>(2);
            parameter.put("token", req.getToken());
            s = HttpUtil.sendPostWithParams(tokenUrl, parameter, Collections.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(s);
    }


    @ApiOperation("获取所有连接")
    @PostMapping("/connection/list")
    public JSONObject listConnections() {
        String token = getGuacamoleToken();
        String queryUrl = String.format("%s/session/data/mysql/connectionGroups/ROOT/tree", baseUrl);
        try {
            Map<String, Object> header = new HashMap<>(1);
            header.put("Guacamole-Token", token);
            String s = HttpUtil.doGet(queryUrl, header, Collections.emptyMap());
            return JSONObject.parseObject(s);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation("获取指定guacamole连接")
    @PostMapping("/connection/{id}/detail")
    public JSONObject getConnection(@PathVariable Long id) {
        String queryBaseUrl = String.format("%s/session/data/mysql/connections/%s", baseUrl, id);
        String queryParametersUrl = String.format("%s/session/data/mysql/connections/%s/parameters", baseUrl, id);
        String token = getGuacamoleToken();
        GetMethod getBaseMethod = new GetMethod(queryBaseUrl);
        getBaseMethod.setRequestHeader("Guacamole-Token", token);
        HttpClient httpClient = new HttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            if (httpClient.executeMethod(getBaseMethod) == 200) {
                GetMethod getParamMethod = new GetMethod(queryParametersUrl);
                getParamMethod.setRequestHeader("Guacamole-Token", token);
                jsonObject = JSONObject.parseObject(getBaseMethod.getResponseBodyAsString());
                if (httpClient.executeMethod(getParamMethod) == 200) {
                    jsonObject.put("parameters", JSONObject.parseObject(getParamMethod.getResponseBodyAsString()));
                }
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取连接失败");
        }
        return jsonObject;
    }


    @ApiOperation("新建连接")
    @PostMapping("/connection/add")
    public String addConnection(@RequestBody ConnectionAddReq req) {
        String addUrl = String.format("%s/session/data/mysql/connections", baseUrl);
        String token = getGuacamoleToken();
        PostMethod postMethod = new PostMethod(addUrl);
        int code;
        String responseBodyAsString;
        try {
            StringRequestEntity stringRequestEntity = new StringRequestEntity(JSONObject.toJSONString(req), "application/json", "utf-8");
            postMethod.setRequestEntity(stringRequestEntity);
            postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            postMethod.setRequestHeader("Guacamole-Token", token);
            code = new HttpClient().executeMethod(postMethod);
            responseBodyAsString = postMethod.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("新建连接异常");
        }
        if (code == 400) {
            throw new RuntimeException(String.format("the connection %s already exists", req.getName()));
        }
        return responseBodyAsString;
    }

    @ApiOperation("编辑连接")
    @PostMapping("/connection/{id}/edit")
    public String editConnection(@PathVariable Long id, @RequestBody ConnectionEditReq req) {
        String editUrl = String.format("%s/session/data/mysql/connections/%s", baseUrl, id);
        String token = getGuacamoleToken();
        PutMethod putMethod = new PutMethod(editUrl);
        int code;
        String responseBodyAsString;
        try {
            StringRequestEntity stringRequestEntity = new StringRequestEntity(JSONObject.toJSONString(req), "application/json", "utf-8");
            putMethod.setRequestEntity(stringRequestEntity);
            putMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            putMethod.setRequestHeader("Guacamole-Token", token);
            code = new HttpClient().executeMethod(putMethod);
            responseBodyAsString = putMethod.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("编辑连接异常");
        }
        if (code == 400) {
            throw new RuntimeException("当前连接不存在或已被删除");
        }
        if (code == 500) {
            throw new RuntimeException("参数或服务器异常");
        }
        return responseBodyAsString;
    }

    @ApiOperation("删除连接")
    @PostMapping("/connection/{id}/delete")
    public String deleteConnection(@PathVariable Long id) {
        String deleteUrl = String.format("%s/session/data/mysql/connections/%s", baseUrl, id);
        String token = getGuacamoleToken();
        HttpClient client = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(deleteUrl);
        deleteMethod.setRequestHeader("Guacamole-Token", token);
        try {
            int code = client.executeMethod(deleteMethod);
            if (code == 404) {
                throw new RuntimeException("连接不存在或已被删除");
            }
            if (code != 200) {
                log.error(deleteMethod.getResponseBodyAsString());
                return deleteMethod.getResponseBodyAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException("删除失败");
        }
        return null;
    }


    //获得当前HttpServletRequest对象
    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    //获取heard中的参数
    private static Map<String, String> getRequestHeaderMap() {
        HttpServletRequest request = getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>(8);
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headerMap.put(name, request.getHeader(name));
        }
        return headerMap;
    }

    /**
     * 获取guacamole token
     *
     * @return token
     */
    private static String getGuacamoleToken() {
        String tokenCache = null;
        String loginUrl = String.format("http://%s/guacamole/api/tokens", "10.31.1.65:9090");
        Map<String, Object> parameter = new HashMap<>(2);
        parameter.put("username", "guacadmin");
        parameter.put("password", "guacadmin");
        try {
            String response = HttpUtil.sendPostWithParams(loginUrl, parameter, Collections.emptyMap());
            JSONObject jsonObject = JSONObject.parseObject(response);
            tokenCache = jsonObject.get("authToken").toString();
        } catch (IOException e) {
            log.error("登录获取token失败", e);
        }
        return tokenCache;
    }
}
