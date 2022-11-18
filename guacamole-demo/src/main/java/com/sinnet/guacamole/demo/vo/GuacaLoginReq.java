package com.sinnet.guacamole.demo.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jiangwentao
 * @description guacamole平台登录
 * @createTime 2022年10月25日 19:07
 */

@Data
@Accessors(chain = true)
public class GuacaLoginReq {
    /**
     * 用户名
     */
    @NotBlank(message = "登录用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "登录密码不能为空")
    private String password;

    private String token;
}
