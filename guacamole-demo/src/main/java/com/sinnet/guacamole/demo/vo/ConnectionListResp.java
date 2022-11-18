package com.sinnet.guacamole.demo.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jiangwentao
 * @description 连接列表
 * @createTime 2022年11月17日 15:54
 */
@Data
@Accessors(chain = true)
public class ConnectionListResp {
    private String name;

    private String identifier;

    private String type;

    private Long activeConnections;

    private List<ChildConnection> childConnections;

    @Data
    public static class ChildConnection {
        private String name;
        private String identifier;
        private String parentIdentifier;
        private String protocol;
        private Long activeConnections;
        private Long lastActive;
        private JSONObject attributes;
    }
}
