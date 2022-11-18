package com.jwt.demo.util.string;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author jiangwentao
 * @version 1.0.0
 * @className StringUtil.java
 * @description String工具类
 * @createTime 2022年03月08日 18:03
 */
public class StringUtil extends StringUtils {

    /**
     * 数字2
     */
    private static final int NUMBER_2 = 2;

    /**
     * 下划线 _
     */
    private static final char UNDER_LINE = '_';

    /**
     * UTF-8字符集
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 0-9和大小写英文字母
     */
    private static final String NUMBER_AND_LETTER = "0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ";

    /**
     * 判断是否为基本类型：包括String
     *
     * @param clazz clazz
     * @return true：是; false：不是
     */
    public static boolean isPrimite(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class;
    }

    /**
     * 获取指定长度随机数
     */
    public static String getRandomNumByLength(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("参数不合法!");
        }
        Random randGen = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(randGen.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * 从指定字符串中，取指定长度的随机字符串
     *
     * @param srcStr 指定的字符串
     * @param length 指定的长度
     * @return 随机字符串
     */
    public static String getRandomStr(String srcStr, int length) {
        if (srcStr == null || "".equals(srcStr) || length < 1) {
            return "";
        }
        char[] numbersAndLetters = srcStr.toCharArray();
        StringBuilder builder = new StringBuilder();
        Random randGen = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(numbersAndLetters[randGen.nextInt(srcStr.length())]);
        }
        return builder.toString();
    }


    /**
     * string转换为字节数组，如"abc"→{97, 98, 99}
     */
    public static byte[] convertString2Bytes(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        try {
            return str.getBytes(CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 字节数组转string，如{97, 98, 99} → "abc"
     */
    public static String convertBytestoString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 获取指定长度16进制随机数
     *
     * @param length 随机数长度
     * @return 随机数
     */
    public static String randomHexInt(int length) {
        if (length < 1) {
            return null;
        }
        Random randGen = new Random();
        String[] str = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        StringBuilder randBuffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randBuffer.append(str[randGen.nextInt(16)]);
        }
        return randBuffer.toString();
    }

    /**
     * int类型转16进制
     */
    public static String intToHex(int num) {
        String hex = Integer.toHexString(num);
        if (hex.length() % NUMBER_2 != 0) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }

    /**
     * 整型转16进制,指定长度，不足补0
     */
    public static String intToHex(int num, int len) {
        String hex = Integer.toHexString(num);
        if (hex.length() % NUMBER_2 != 0) {
            hex = "0" + hex;
        }
        if (hex.length() < len) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < len - hex.length(); i++) {
                b.append("0");
            }
            hex = b.append(hex).toString();
        }
        return hex.toUpperCase();
    }

    /**
     * 16进制转int
     */
    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * 带下划线的字符串转成驼峰形式，如"hello_world" → "helloWorld"
     *
     * @param str 带下划线的字符串
     * @return 驼峰形式
     */
    public static String toCamelNaming(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str.length());
        boolean upperCase = false;
        // 将目标字符串转成小写
        str = str.toLowerCase();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            //如果为下划线，则将upperCase置为true，表示下一个字母需转成大写
            if (c == UNDER_LINE) {
                upperCase = true;
            } else {
                if (upperCase) {
                    //下滑线后的字母转成大写拼接
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    //其他字母直接拼接
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 带下划线的字符串转成首字母大写的驼峰形式，如"hello_world" → "HelloWorld"
     *
     * @param str 带下划线的字符串
     * @return 首字母大写的驼峰形式
     */
    public static String toCamelNamingCapitalize(String str) {
        if (str == null) {
            return null;
        }
        str = toCamelNaming(str);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 驼峰形式的字符串转成下划线形式，如"helloWorld" → "hello_world"
     *
     * @param str 带驼峰形式的字符串
     * @return 下划线形式
     */
    public static String toUnderlineCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            boolean nextUpperCase = true;
            if (i < str.length() - 1) {
                nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(UNDER_LINE);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
