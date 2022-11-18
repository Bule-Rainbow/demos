package com.jwt.demo.util.secret;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;

/**
 * @author jiangwentao
 * @description 加密解密测试
 * @createTime 2022年09月29日 16:00
 */
public class SecretTest {
    public static void main(String[] args) {

        String rsaPublicStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkif35FwivUz6UEpeg9ki5BEfifoqTE9nbDtpcP7dalvSXQXP1N9BuInApvneEIrttfOEHkfm2HSEGr2J1hEYnCAxXQXQDdcxVFeIaq5sg+zXCowaHlaY5j3ZOjU+eQ00KQMKtvwD+5jTzf4shBLCuLG5dBdoEmIYuowtPtn1HdwIDAQAB";
        String rsaPrivateStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKSJ/fkXCK9TPpQSl6D2SLkER+J+ipMT2dsO2lw/t1qW9JdBc/U30G4icCm+d4Qiu2184QeR+bYdIQavYnWERicIDFdBdAN1zFUV4hqrmyD7NcKjBoeVpjmPdk6NT55DTQpAwq2/AP7mNPN/iyEEsK4sbl0F2gSYhi6jC0+2fUd3AgMBAAECgYEAm1rrYSBdg+UuhfLUFhfUbFTdZr6iKuKoZUwt4sIKZk9328TiLtunQgXkL5X2FN4SZ/tsIZCz3T69qyk35sugy7xh66OkmjBQIqKJNYXoX82v+wzkNFsyP/0OfQdCNWX1RNUOmcZC45gKXh0BPCYiQj/GvsS0Ogn9EIWjnONlozECQQDrwwsd+a5H+EUP0CqsWkreFP7VyQF6DxwCmjTHWIPraIPUiMFMLxO9nJ4GYou8iRkbI5EsnFIsdc3nYdrSfg2PAkEAsqnMqeqQmcKPB7RyaR0s/AckBNibPQtooiBstO+ZgDCBAHYMhAtOE7tWWUsIPPQrPHAl4OHbwDyzYZnIdtmDmQJBAJ8P4JhAtEzqO+4HTEMsSAa9s5EZJF3tg8Bdue4k0hwpT4g9G+0Cdhcfa55zLkPtW3rr7C5k6SwF7v2FD9WkJhECQDNTOjtZ6j5xuPsiqHdv7TtdpchKnZYUz/M7U19LMZK03GpQf1nzkXNzruOp/WzdTAppwUfwOlvoVDPmpMMXy4kCQFwPqTGDLZKUoLOMSbF5BdHb5PJcKL6Iu3PGAcr2HkPcSsjItNLiwlmBO1zLl1Gg0QH8O9himEnpUhBwYZTzaw8=";

        //String pwd = "jwt2020.";
        String pwd = "jwt2020.";
        System.out.println("原密码：" + pwd);

        //RSA加解密
        byte[] encrypt = SecureUtil.rsa(null, rsaPublicStr).encrypt(pwd, KeyType.PublicKey);
        String encryptStr = HexUtil.encodeHexStr(encrypt);
        String decryptStr = new String(SecureUtil.rsa(rsaPrivateStr, null).decrypt(encryptStr, KeyType.PrivateKey));
        System.out.println("RSA公钥加密：" + encryptStr);
        System.out.println("RSA私钥解密：" + decryptStr);

        //RSA加解密+Base64加解密
        String encryptWithRsa = SecretUtil.encryptRsaByPublicKey(rsaPublicStr, pwd);
        System.out.println("RSA公钥加密+BASE64加密：" + encryptWithRsa);
        System.out.println("RSA私钥解密+BASE64解密：" + SecretUtil.decryptRsaByPrivateKey(rsaPrivateStr, encryptWithRsa));


        //AES加密、解密
        String salt = "7i42WnLrHWy8laAB";
        //AES加密
        String aesEncryptStr = SecretUtil.aesEncrypt(salt, pwd);
        System.out.println("AES加密：" + aesEncryptStr);
        //AES解密
        String decryptData = SecretUtil.aesDecrypt(salt, aesEncryptStr);
        System.out.println("AES解密：" + decryptData);

        RsaKeyPairInStr rsaKeyPairInStr = SecretUtil.generateRsaKeyPair(1024);
        System.out.println("生成RSA公私钥对:"+rsaKeyPairInStr);
        System.out.println("公钥："+rsaKeyPairInStr.getPublicKey());
        System.out.println("私钥："+rsaKeyPairInStr.getPrivateKey());
        System.out.println("私钥长度："+rsaKeyPairInStr.getPrivateKey().length());

    }
}
