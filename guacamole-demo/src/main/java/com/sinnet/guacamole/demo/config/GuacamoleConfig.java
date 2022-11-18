package com.sinnet.guacamole.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiangwentao
 * @description
 * @createTime 2022年10月27日 9:50
 */
@Data
@Component
public class GuacamoleConfig {

    @Value("${guacamole.serverIp}")
    private String serverIp;

    @Value("${guacamole.serverPort}")
    private int serverPort;

    @Value("${guacamole.username}")
    private String username;

    @Value("${guacamole.password}")
    private String password;

}
