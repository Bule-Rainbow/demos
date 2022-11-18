package com.jwt.demo.util.totp.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author jiangwentao
 * @description mfa校验请求参数
 * @createTime 2022年09月29日 11:02
 */
@Data
@Accessors(chain = true)
public class MfaCheckReq {

    /**
     * MFA验证码
     */
    @NotBlank(message = "MFA验证码不能为空")
    @Pattern(regexp = "[0-9]{6}", message = "验证码仅支持6位数字")
    private String code;

    /**
     * 密钥
     */
    private String secretKey;
}
