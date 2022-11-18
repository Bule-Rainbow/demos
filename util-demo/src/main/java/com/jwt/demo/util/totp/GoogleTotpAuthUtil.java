package com.jwt.demo.util.totp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author jiangwentao
 * @description 谷歌身份认证totp
 * @createTime 2022年09月29日 10:30
 */
public class GoogleTotpAuthUtil {

    public static final int SECRET_SIZE = 32;

    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";

    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    private static final int WINDOW_SIZE_MIN = 1;

    private static final int WINDOW_SIZE_MAX = 17;

    /**
     * default 3 - max 17 (from google docs)最多可偏移的时间
     */
    int windowSize = 1;

    /**
     * set the windows size. This is an integer value representing the number of 30 second windows
     * we allow
     * The bigger the window, the more tolerant of clock skew we are.
     *
     * @param s window size - must be >=1 and <=17. Other values are ignored
     */
    public void setWindowSize(int s) {
        if (s >= WINDOW_SIZE_MIN && s <= WINDOW_SIZE_MAX) {
            windowSize = s;
        }
    }

    /**
     * Generate a random secret key. This must be saved by the server and associated with the
     * users account to verify the code displayed by Google Authenticator.
     * The user must register this secret on their device.
     *
     * @return secret key
     */
    public static String generateSecretKey() {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            secureRandom.setSeed(Base64.getDecoder().decode(SEED));
            byte[] buffer = secureRandom.generateSeed(SECRET_SIZE);
            byte[] bEncodedKey = Base32.encode(buffer).getBytes();
            return new String(bEncodedKey);
        } catch (NoSuchAlgorithmException e) {
            // should never occur... configuration error
        }
        return null;
    }

    /**
     * Return a URL that generates and displays a QR barcode. The user scans this bar code with the
     * Google Authenticator application on their smartphone to register the auth code. They can also
     * manually enter the
     * secret if desired
     *
     * @param user   user id (e.g. fflinstone)
     * @param host   host or system that the code is for (e.g. myapp.com)
     * @param secret the secret that was previously generated for this user
     * @return the URL for the QR code to scan
     */
    public static String getQrCodeUrl(String user, String host, String secret, String title) {
        String url = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
        return String.format(url, user, host, secret, title);
    }

    /**
     * Check the code entered by the user to see if it is valid
     *
     * @param secret   The users secret.
     * @param code     The code displayed on the users device
     * @param timeMsec The time in msec (System.currentTimeMillis() for example)
     * @return 验证结果
     * @throws Exception 处理异常
     */
    public boolean checkCode(String secret, String code, long timeMsec) throws Exception {
        byte[] decodedKey = Base32.decode(secret);
        // convert unix msec time into a 30 second "window"
        // this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / 30L;
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        for (int i = -windowSize; i <= windowSize; ++i) {
            long hash;
            try {
                hash = genVerifyCode(decodedKey, t + i);
            } catch (Exception e) {
                // Yes, this is bad form - but
                // the exceptions thrown would be rare and a static configuration problem
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
                //return false;
            }
            if (String.valueOf(hash).equals(code)) {
                return true;
            }
        }
        // The validation code is invalid.
        return false;
    }

    public int queryCurrentCode(String secret, long timeMsec) throws Exception {
        Base32 codec = new Base32();
        byte[] decodedKey = Base32.decode(secret);
        long t = (timeMsec / 1000L) / 30L;
        return genVerifyCode(decodedKey, t);
    }


    private static int genVerifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }
}

