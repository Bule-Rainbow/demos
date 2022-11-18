package com.sinnet.guacamole.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @author jiangwentao
 * @description 客户端
 * @createTime 2022年10月27日 14:14
 */
@Slf4j
public class GuacamoleSocketClient extends WebSocketClient {

    public GuacamoleSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("CMP to guacamole webSocket open");
    }

    @Override
    public void onMessage(String s) {
        //log.info("message from guacamole to web");
        //将guacamole服务端发来的消息，转发到页面客户端
        RemoteDesktopSocket.sendMsg(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("guacamole webSocket close");
    }

    @Override
    public void onError(Exception e) {
        log.error("guacamole webSocket error", e);
    }
}
