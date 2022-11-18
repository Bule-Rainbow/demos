package com.jwt.demo.util.totp;

import com.jwt.demo.util.totp.vo.MfaCheckReq;
import com.jwt.demo.util.totp.vo.MfaInfoResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className MfaController.java
 * @description mfa安全验证，基于谷歌Google TOTP实现
 * @createTime 2022年09月29日 10:26
 */
@RestController
@RequestMapping("/mfa")
public class MfaTest {

    @GetMapping("/info")
    public MfaInfoResp info() {
        String account = "969235344@qq.com";
        String secretKey = GoogleTotpAuthUtil.generateSecretKey();
        String qrCodeUrl = GoogleTotpAuthUtil.getQrCodeUrl("jwt", account, secretKey, "whty");
        return new MfaInfoResp().setAccount(account).setSecretKey(secretKey).setQrCodeUrl(qrCodeUrl);
    }

    @PostMapping("/check")
    public String check(@Valid @RequestBody MfaCheckReq req) {
        GoogleTotpAuthUtil googleAuthenticator = new GoogleTotpAuthUtil();
        boolean verified = false;
        try {
            verified = googleAuthenticator.checkCode(req.getSecretKey(), req.getCode(), System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verified ? "1" : "0";
    }
}
