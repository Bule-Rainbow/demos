package com.sinnet.guacamole.demo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author jiangwentao
 * @description 编辑连接参数
 * @createTime 2022年10月26日 9:02
 */
@Data
@Accessors(chain = true)
public class ConnectionEditReq {
    /**
     * 名称
     */
    private String name;

    /**
     * 位置
     */
    private String parentIdentifier;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 连接属性
     */
    private Map<String, String> attributes;

    /**
     * 连接参数
     */
    private Map<String, String> parameters;
}
