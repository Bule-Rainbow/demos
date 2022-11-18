package com.jwt.demo.util.totp.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiangwentao
 * @description 用户获取mfa信息
 * @createTime 2022年09月29日 10:30
 */
@Data
@Accessors(chain = true)
public class MfaInfoResp {

    /**
     * 账号，一般使用邮箱或者用户名，保证唯一
     */
    private String account;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 二维码地址
     */
    private String qrCodeUrl;

}
