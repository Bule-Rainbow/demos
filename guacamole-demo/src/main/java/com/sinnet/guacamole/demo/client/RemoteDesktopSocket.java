package com.sinnet.guacamole.demo.client;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.sinnet.guacamole.demo.config.GuacamoleConfig;
import com.sinnet.guacamole.demo.factory.SpringBeanFactory;
import com.sinnet.guacamole.demo.util.HttpUtil;
import com.sinnet.guacamole.demo.util.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.enums.ReadyState;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangwentao
 * @description
 * @createTime 2022年10月27日 14:16
 */
@Slf4j
@Component
@ServerEndpoint(value = "/ws/guacamole/{guacdId}", subprotocols = "guacamole")
public class RemoteDesktopSocket {

    private static final Cache<String, String> TOKEN_CACHE = new Cache<>();

    private final GuacamoleConfig guacaMoleConfig = (GuacamoleConfig) SpringBeanFactory.getBean("guacamoleConfig");

    private static Session webSession;

    private static GuacamoleSocketClient guacaClient;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "guacdId") String guacdId) throws Exception {
        log.info("web to CMP webSocket open");
        webSession = session;
        //获取guacamole token
        String token = getToken(guacaMoleConfig);
        //获取连接guacamole的websocket url
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        String wsUrl = getWsUrl(guacaMoleConfig.getServerIp(), token, guacdId, requestParameterMap);
        GuacamoleSocketClient myClient = new GuacamoleSocketClient(new URI(wsUrl));
        guacaClient = myClient;
        myClient.connect();
        int connectionCount = 1;
        while (!ReadyState.OPEN.equals(myClient.getReadyState()) && connectionCount <= 3) {
            log.info(String.format("第%s次连接中。。。", connectionCount));
            connectionCount++;
            Thread.sleep(3000);
        }
        if (connectionCount > 3) {
            log.error(String.format("websocket连接失败，GUC_ID:%s", guacdId));
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("web webSocket close");
        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {

            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        //log.info("message from web to guacamole");
        //将页面客户端的消息，转发到guacamole服务端
        guacaClient.send(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("web webSocket error");
        this.onClose(session);
    }

    /**
     * 将guacamole服务端发来的消息，转发到页面客户端
     *
     * @param msg 消息
     */
    public static void sendMsg(String msg) {
        try {
            if (webSession.isOpen()) {
                webSession.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {

        }
    }

    /**
     * 拼接访问guacamole服务端websocket的url
     *
     * @param serverIp 服务端IP
     * @param token    服务端登录token
     * @return url
     */
    private static String getWsUrl(String serverIp, String token, String guacId, Map<String, List<String>> requestParameterMap) {
        String url = String.format("ws://%s:9090/guacamole/websocket-tunnel?token=%s", serverIp, token);
        String dpi = "96";
        List<String> dpis = requestParameterMap.get("dpi");
        if (CollectionUtil.isNotEmpty(dpis) && dpis.get(0).matches("[0-9]+")) {
            dpi = dpis.get(0);
        }
        String width = "1920";
        List<String> widths = requestParameterMap.get("width");
        if (CollectionUtil.isNotEmpty(widths) && widths.get(0).matches("[0-9]+")) {
            width = widths.get(0);
        }
        String height = "800";
        List<String> heights = requestParameterMap.get("height");
        if (CollectionUtil.isNotEmpty(heights) && heights.get(0).matches("[0-9]+")) {
            height = heights.get(0);
        }
        return url +
                "&GUAC_ID=" + guacId +
                "&GUAC_DPI=" + dpi +
                "&GUAC_WIDTH=" + width +
                "&GUAC_HEIGHT=" + height +
                "&GUAC_TYPE=" + "c" +
                "&GUAC_DATA_SOURCE=" + "mysql" +
                "&GUAC_TIMEZONE=" + "Asia/Shanghai" +
                "&GUAC_AUDIO=" + "audio/L8" +
                "&GUAC_AUDIO=" + "audio/L16" +
                "&GUAC_IMAGE=" + "image/jpeg" +
                "&GUAC_IMAGE=" + "image/png" +
                "&GUAC_IMAGE=" + "image/webp";
    }

    /**
     * 获取guacamole token
     *
     * @param guacaMoleConfig 基础信息
     * @return token
     */
    private static String getToken(GuacamoleConfig guacaMoleConfig) {
        String tokenCache = TOKEN_CACHE.get(guacaMoleConfig.getUsername());
        if (tokenCache == null) {
            String loginUrl = String.format("http://%s/guacamole/api/tokens", guacaMoleConfig.getServerIp());
            Map<String, Object> parameter = new HashMap<>(2);
            parameter.put("username", guacaMoleConfig.getUsername());
            parameter.put("password", guacaMoleConfig.getPassword());
            try {
                String response = HttpUtil.sendPostWithParams(loginUrl, parameter, Collections.emptyMap());
                JSONObject jsonObject = JSONObject.parseObject(response);
                tokenCache = jsonObject.get("authToken").toString();
                TOKEN_CACHE.put(guacaMoleConfig.getUsername(), tokenCache, 10, TimeUnit.MINUTES);
            } catch (IOException e) {
                log.error("登录获取token失败", e);
            }
        }
        return tokenCache;
    }


}
