package com.jwt.demo.util.secret;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.symmetric.AES;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.KeyPair;
import java.security.SecureRandom;

/**
 * @description: 加密与解密
 * @author: jiangwentao
 * @email: jiangwentao@whty.com.cn
 * @date: 2022/1/7 15:11
 */
public class SecretUtil {
    private static final Key key;
    private static final String KEY_STR = "whty@123";
    private static final String CHARSETNAME = "UTF-8";
    private static final String ALGORITHM = "DES";
    private static final String ALGORITHM_NAME = "SHA1PRNG";

    static {
        try {
            //生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            //运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance(ALGORITHM_NAME);
            //设置上密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            //初始化基于SHA1的算法对象
            generator.init(secureRandom);
            //生成密钥对象
            key = generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * base64加密
     * @param str 明文
     * @return 密文
     */
    public static String encryption(String str) {
        //基于BASE64编码，接收byte[]并转换成String
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            //按utf8编码
            byte[] bytes = str.getBytes(CHARSETNAME);
            //获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //加密
            byte[] doFinal = cipher.doFinal(bytes);
            //byte[]to encode好的String 并返回
            return encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * base64解密
     * @param str 密文
     * @return 明文
     */
    public static String decrypt(String str) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //将字符串decode成byte[]
            byte[] bytes = decoder.decodeBuffer(str);
            //获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            //解密
            byte[] doFial = cipher.doFinal(bytes);
            return new String(doFial, CHARSETNAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用base64加密
     *
     * @return
     */
    public static String encryptWithBase64(String data) {
        return Base64.encode(data);
    }

    /**
     * base64解码
     *
     * @param base64Encrypted
     * @return
     */
    public static String decryptedWithBase64(String base64Encrypted) {
        return Base64.decodeStr(base64Encrypted);
    }

    /**
     * AES加密
     *
     * @param aesKey  加密key
     * @param dataStr 待加密字符串
     * @return 加密后字符串
     */
    public static String aesEncrypt(String aesKey, String dataStr) {
        AES aes = SecureUtil.aes(StrUtil.bytes(aesKey, CharsetUtil.CHARSET_UTF_8));
        byte[] encrypted = aes.encrypt(dataStr);
        return Base64.encode(encrypted);

    }

    /**
     * AES解密
     *
     * @param aesKey          解密key
     * @param encryptedBase64 待解密字符串
     * @return 解密后字符串
     */
    public static String aesDecrypt(String aesKey, String encryptedBase64) {
        AES aes = SecureUtil.aes(StrUtil.bytes(aesKey, CharsetUtil.CHARSET_UTF_8));
        byte[] decrypted = aes.decrypt(Base64.decode(encryptedBase64));
        return new String(decrypted, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 生成一对RSA公私要对
     * 数据以BASE64加密的字符串呈现
     *
     * @param length
     * @return
     */
    public static RsaKeyPairInStr generateRsaKeyPair(int length) {
        KeyPair keyPair = SecureUtil.generateKeyPair("RSA", length);
        String privateKey = Base64.encode(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.encode(keyPair.getPublic().getEncoded());
        return RsaKeyPairInStr.builder()
                .privateKey(privateKey)
                .publicKey(publicKey)
                .build();
    }

    /**
     * 以sha256方式计算数据的hash值
     *
     * @param data
     * @return
     */
    public static String calcHashInSha256(String data) {
        return SecureUtil.sha256(data);
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        return RandomUtil.randomString(length);
    }


    /**
     * aes方式加密
     *
     * @param key
     * @param data
     * @return
     */
    public static String encryptWithAes(String key, String data) {
        return SecureUtil.aes(key.getBytes()).encryptBase64(data.getBytes());
    }

    /**
     * aes方式解密
     *
     * @param key
     * @param dataEncrypt
     * @return
     */
    public static String decryptWithAes(String key, String dataEncrypt) {
        return SecureUtil.aes(key.getBytes()).decryptStr(dataEncrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 使用公钥加密数据
     *
     * @param publicKey
     * @param data
     * @return
     */
    public static String encryptRsaByPublicKey(String publicKey, String data) {
        byte[] encrypt = SecureUtil.rsa(null, publicKey).encrypt(data, KeyType.PublicKey);
        String hexStr = HexUtil.encodeHexStr(encrypt);
        return Base64.encode(hexStr);
    }

    /**
     * 使用公钥解密数据
     *
     * @param publicKey
     * @param data
     * @return
     */
    public static String decryptRsaByPublicKey(String publicKey, String data) {
        String hexString = decryptedWithBase64(data);
        byte[] hexData = HexUtil.decodeHex(hexString);
        return new String(SecureUtil.rsa(null, publicKey).decrypt(hexData, KeyType.PublicKey));
    }

    /**
     * RSA私钥加密
     *
     * @param privateKey
     * @param data
     * @return
     */
    public static String encryptRsaByPrivateKey(String privateKey, String data) {
        byte[] byteData = SecureUtil.rsa(privateKey, null).encrypt(data.getBytes(), KeyType.PrivateKey);
        String hexData = HexUtil.encodeHexStr(byteData);
        return Base64.encode(hexData);
    }

    /**
     * RSA私钥解密
     *
     * @param privateKey    私钥
     * @param encryptedData 被公钥加密的数据
     * @return
     */
    public static String decryptRsaByPrivateKey(String privateKey, String encryptedData) {
        String base64DecodeData = Base64.decodeStr(encryptedData);
        byte[] hex = HexUtil.decodeHex(base64DecodeData);
        return new String(SecureUtil.rsa(privateKey, null).decrypt(hex, KeyType.PrivateKey));
    }
}
