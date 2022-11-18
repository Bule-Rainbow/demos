package com.jwt.demo.util.ip;

import cn.hutool.core.net.Ipv4Util;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: IP工具类
 * @author: jiangwentao
 * @email: jiangwentao@whty.com.cn
 * @date: 2021/12/14 14:09
 */
public class IPUtil {

    /**
     * 正则：IPV4地址格式
     */
    private static final String REGEX_IPV4_ADDRESS = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";
    /**
     * 正则：IPV4带掩码地址格式
     */
    private static final String REGEX_IPV4_CIDR = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)[/](\\d|[1-2]\\d|3[0-2])";
    /**
     * 正则：IPV6地址格式
     */
    private static final String REGEX_IPV6_ADDRESS = "([a-f0-9]{1,4}(:[a-f0-9]{1,4}){7}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){0,7}::[a-f0-9]{0,4}(:[a-f0-9]{1,4}){0,7})";
    /**
     * 正则：IPV6带掩码地址格式
     */
    private static final String REGEX_IPV6_CIDR = "([a-f0-9]{1,4}(:[a-f0-9]{1,4}){7}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){0,7}::[a-f0-9]{0,4}(:[a-f0-9]{1,4}){0,7})[/](\\d|[1-9]\\d|1[0-1]\\d|12[0-8])";
    /**
     * 正则：IPV4和IPV6通用，带掩码格式
     */
    private static final String REGEX_IPV4_IPV6_CIDR = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/(\\d|[1-2]\\d|3[0-2]))?$|([a-f0-9]{1,4}(:[a-f0-9]{1,4}){7}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){0,7}::[a-f0-9]{0,4}(:[a-f0-9]{1,4}){0,7})[/](\\d|[1-9]\\d|1[0-1]\\d|12[0-8])";


    public static void main(String[] args) throws Exception {
        Ipv4Util.list("10.10.10.0/24",true);
        parseIpMaskRange("10.10.10.0","24");
        String number = "2001:bca8::/128";
        String number1 = "10.10.10.0/24";
        System.out.println(number1.matches(REGEX_IPV4_IPV6_CIDR));
        System.out.println(getLocalhostIpAddress());
        System.out.println(getLocalhostMacAddress());
    }

    /**
     * ping指定IP
     *
     * @param ipAddress ip地址
     * @param pingTimes ping的次数
     * @param timeOut   超时时间
     * @return true 能ping通 false 不通
     */
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        String osName = System.getProperty("os.name");
        String pingCommand;
        if (osName.contains("Linux")) {
            //ping -c pingTimes -w timeOut ipAddress;
            pingCommand = "ping -c " + pingTimes + " -w " + timeOut + "" + ipAddress;
        } else {
            //ping ipAddress -n pingTimes -w timeOut;
            pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
        }
        System.out.println("ping command：" + pingCommand);
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        try {
            // 执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line;
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
     *
     * @param line 待检测行
     * @return 结果
     */
    private static int getCheckResult(String line) {
        String regex = "(\\d+ms)(\\s+)(TTL=\\d+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return 1;
        }
        return 0;
    }


    /**
     * 获取本机IP地址
     *
     * @return ip地址
     * @throws UnknownHostException 主机未知
     */
    public static String getLocalhostIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取本机mac地址（大写），如 A4-AE-12-85-57-CE
     *
     * @return mac地址
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static String getLocalhostMacAddress() throws UnknownHostException, SocketException {
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0").append(str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * @description: IPV4地址转换成Long类型数字，如172.10.10.1 → 2886339073
     * @author: jiangwentao
     * @date: 2021/12/27 13:46
     */
    public static Long transIpv4ToLong(String ipv4Address) {
        long number = 0;
        if (StringUtils.isEmpty(ipv4Address)) {
            return number;
        }
        String[] ipSplit = ipv4Address.split("\\.");
        for (int i = 0; i < ipSplit.length; i++) {
            number += Integer.parseInt(ipSplit[i]) * Math.pow(256, (3 - i));
        }
        return number;
    }

    /**
     * @description: long类型数字转成IPV4地址，如2886339073 → 172.10.10.1
     * @author: jiangwentao
     * @date: 2021/12/27 13:32
     */
    public static String transLongToIPV4(Long number) {
        String sa = String.valueOf((number >> 24) % 256);
        String sb = String.valueOf((number >> 16) % 256);
        String sc = String.valueOf((number >> 8) % 256);
        String sd = String.valueOf(number % 256);
        return sa + "." + sb + "." + sc + "." + sd;
    }

    /**
     * @description: long类型数字转成IPV4地址，如2886339073 → 172.10.10.1
     * @author: jiangwentao
     * @date: 2021/12/27 13:32
     */
    public static String getIpFromLong(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * ipv6字符串转BigInteger数
     */
    public static BigInteger ipv6ToInt(String ipv6) {
        int compressIndex = ipv6.indexOf("::");
        if (compressIndex != -1) {
            String part1s = ipv6.substring(0, compressIndex);
            String part2s = ipv6.substring(compressIndex + 1);
            BigInteger part1 = ipv6ToInt(part1s);
            BigInteger part2 = ipv6ToInt(part2s);
            int part1hasDot = 0;
            char[] ch = part1s.toCharArray();
            for (char c : ch) {
                if (c == ':') {
                    part1hasDot++;
                }
            }
            return part1.shiftLeft(16 * (7 - part1hasDot)).add(part2);
        }
        String[] str = ipv6.split(":");
        BigInteger big = BigInteger.ZERO;
        for (int i = 0; i < str.length; i++) {
            //::1
            if (str[i].isEmpty()) {
                str[i] = "0";
            }
            big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16)).shiftLeft(16 * (str.length - i - 1)));
        }
        return big;
    }

    /**
     * BigInteger数 转为ipv6字符串
     */
    public static String intToIpv6(BigInteger big) {
        StringBuilder str = new StringBuilder();
        BigInteger ff = BigInteger.valueOf(0xffff);
        for (int i = 0; i < 8; i++) {
            str.insert(0, big.and(ff).toString(16) + ":");
            big = big.shiftRight(16);
        }
        //去掉最后的：号
        str = new StringBuilder(str.substring(0, str.length() - 1));
        return str.toString().replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
    }

    /**
     * 将精简的ipv6地址扩展为全长度的ipv6地址
     */
    public static String completeIpv6(String strIpv6) {
        BigInteger big = ipv6ToInt(strIpv6);
        StringBuilder str = new StringBuilder(big.toString(16));
        StringBuilder completeIpv6Str = new StringBuilder();
        while (str.length() != 32) {
            str.insert(0, "0");
        }
        for (int i = 0; i <= str.length(); i += 4) {
            completeIpv6Str.append(str.substring(i, i + 4));
            if ((i + 4) == str.length()) {
                break;
            }
            completeIpv6Str.append(":");
        }
        return completeIpv6Str.toString();
    }


    /**
     * 功能：判断一个IPV4地址是不是在一个网段下的
     * 格式：isInRange("192.168.8.3", "192.168.9.10/22");
     */
    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    /**
     * 功能：根据IP和位数返回该IP网段的所有IP，可用Ipv4Util.list(ip/mask,false)代替
     * 格式：parseIpMaskRange("192.192.192.1", "23")
     */
    public static List<String> parseIpMaskRange(String ip, String mask) {
        List<String> list = new ArrayList<>();
        if ("32".equals(mask)) {
            list.add(ip);
        } else {
            String startIp = getBeginIpStr(ip, mask);
            String endIp = getEndIpStr(ip, mask);
            if (!"31".equals(mask)) {
                String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
                String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
                startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
                endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
            }
            list = parseIpRange(startIp, endIp);
        }
        return list;
    }

    /**
     * 功能：根据位数返回IP总数
     * 格式：parseIpMaskRange("192.192.192.1", "23")
     */
    public static int getIpCount(String mask) {
        //IP总数，去小数点
        return BigDecimal.valueOf(Math.pow(2, 32 - Integer.parseInt(mask))).setScale(0, BigDecimal.ROUND_DOWN).intValue();
    }

    /**
     * 功能：判断是否为IPV4地址
     *
     * @param str 字符串
     * @return 结果
     */
    public static boolean isIpV4Address(String str) {
        if (str != null && !str.isEmpty()) {
            // 判断ip地址是否与正则表达式匹配
            return str.matches(REGEX_IPV4_ADDRESS);
        }
        return false;
    }

    /**
     * 功能：判断是否为IPV4 CIDR
     *
     * @param str 字符串
     * @return 结果
     */
    public static boolean isIpv4Cidr(String str) {
        if (str != null && !str.isEmpty()) {
            // 判断ip地址是否与正则表达式匹配
            return str.matches(REGEX_IPV4_CIDR);
        }
        return false;
    }

    public static boolean isIpv6Addr(String text) {
        if (text != null && !text.isEmpty()) {
            // 判断ip地址是否与正则表达式匹配
            return text.matches(REGEX_IPV6_ADDRESS);
        }
        return false;
    }

    public static boolean isIpv6Cidr(String text) {
        if (text != null && !text.isEmpty()) {
            // 判断ip地址是否与正则表达式匹配
            return text.matches(REGEX_IPV6_CIDR);
        }
        return false;
    }

    public static boolean isIpv4OrIpv6Cidr(String text) {
        if (text != null && !text.isEmpty()) {
            // 判断ip地址是否与正则表达式匹配
            return text.matches(REGEX_IPV4_IPV6_CIDR);
        }
        return false;
    }

    /**
     * 功能: 获取两个IP之间的所有IP(闭区间)
     * 格式：parseIpRange("10.10.15.2","10.10.15.20")
     */
    public static List<String> parseIpRange(String ipfrom, String ipto) {
        List<String> ips = new ArrayList<>();
        String[] ipfromd = ipfrom.split("\\.");
        String[] iptod = ipto.split("\\.");
        int[] int_ipf = new int[4];
        int[] int_ipt = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(ipfromd[i]);
            int_ipt[i] = Integer.parseInt(iptod[i]);
        }
        for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
            for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1]
                    : 255); B++) {
                for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2]
                        : 255); C++) {
                    for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3]
                            : 255); D++) {
                        ips.add(A + "." + B + "." + C + "." + D);
                    }
                }
            }
        }
        return ips;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param maskBit 掩码位数，如"28"、"30"
     * @return 掩码
     */
    public static String getMaskByMaskBit(String maskBit) {
        return "".equals(maskBit) ? "error, maskBit is null !" : getMaskMap(maskBit);
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的字符串表示
     */
    public static String getBeginIpStr(String ip, String maskBit) {
        return getIpFromLong(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的长整型表示
     */
    private static Long getBeginIpLong(String ip, String maskBit) {
        return transIpv4ToLong(ip) & transIpv4ToLong(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的字符串表示
     */
    public static String getEndIpStr(String ip, String maskBit) {
        return getIpFromLong(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的长整型表示
     */
    private static Long getEndIpLong(String ip, String maskBit) {
        return getBeginIpLong(ip, maskBit)
                + ~transIpv4ToLong(getMaskByMaskBit(maskBit));
    }


    /**
     * 根据子网掩码转换为掩码位 如 255.255.255.252转换为掩码位 为 30
     *
     * @param netmarks 子网掩码
     * @return 掩码位
     */
    public static int getNetMask(String netmarks) {
        StringBuilder sbf;
        String str;
        int inetmask = 0;
        int count;
        String[] ipList = netmarks.split("\\.");
        for (String s : ipList) {
            sbf = toBin(Integer.parseInt(s));
            str = sbf.reverse().toString();
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf('1', i);
                if (i == -1) {
                    break;
                }
                count++;
            }
            inetmask += count;
        }
        return inetmask;
    }

    /**
     * 计算子网大小
     *
     * @param maskBit 掩码位
     * @return 子网大小
     */
    public static int getPoolMax(int maskBit) {
        if (maskBit <= 0 || maskBit >= 32) {
            return 0;
        }
        return (int) Math.pow(2, 32 - maskBit) - 2;
    }

    private static StringBuilder toBin(int x) {
        StringBuilder result = new StringBuilder();
        result.append(x % 2);
        x /= 2;
        while (x > 0) {
            result.append(x % 2);
            x /= 2;
        }
        return result;
    }

    public static String getMaskMap(String maskBit) {
        if ("1".equals(maskBit)) {
            return "128.0.0.0";
        }
        if ("2".equals(maskBit)) {
            return "192.0.0.0";
        }
        if ("3".equals(maskBit)) {
            return "224.0.0.0";
        }
        if ("4".equals(maskBit)) {
            return "240.0.0.0";
        }
        if ("5".equals(maskBit)) {
            return "248.0.0.0";
        }
        if ("6".equals(maskBit)) {
            return "252.0.0.0";
        }
        if ("7".equals(maskBit)) {
            return "254.0.0.0";
        }
        if ("8".equals(maskBit)) {
            return "255.0.0.0";
        }
        if ("9".equals(maskBit)) {
            return "255.128.0.0";
        }
        if ("10".equals(maskBit)) {
            return "255.192.0.0";
        }
        if ("11".equals(maskBit)) {
            return "255.224.0.0";
        }
        if ("12".equals(maskBit)) {
            return "255.240.0.0";
        }
        if ("13".equals(maskBit)) {
            return "255.248.0.0";
        }
        if ("14".equals(maskBit)) {
            return "255.252.0.0";
        }
        if ("15".equals(maskBit)) {
            return "255.254.0.0";
        }
        if ("16".equals(maskBit)) {
            return "255.255.0.0";
        }
        if ("17".equals(maskBit)) {
            return "255.255.128.0";
        }
        if ("18".equals(maskBit)) {
            return "255.255.192.0";
        }
        if ("19".equals(maskBit)) {
            return "255.255.224.0";
        }
        if ("20".equals(maskBit)) {
            return "255.255.240.0";
        }
        if ("21".equals(maskBit)) {
            return "255.255.248.0";
        }
        if ("22".equals(maskBit)) {
            return "255.255.252.0";
        }
        if ("23".equals(maskBit)) {
            return "255.255.254.0";
        }
        if ("24".equals(maskBit)) {
            return "255.255.255.0";
        }
        if ("25".equals(maskBit)) {
            return "255.255.255.128";
        }
        if ("26".equals(maskBit)) {
            return "255.255.255.192";
        }
        if ("27".equals(maskBit)) {
            return "255.255.255.224";
        }
        if ("28".equals(maskBit)) {
            return "255.255.255.240";
        }
        if ("29".equals(maskBit)) {
            return "255.255.255.248";
        }
        if ("30".equals(maskBit)) {
            return "255.255.255.252";
        }
        if ("31".equals(maskBit)) {
            return "255.255.255.254";
        }
        if ("32".equals(maskBit)) {
            return "255.255.255.255";
        }
        return "-1";
    }

    public static double ipToDouble(String ip) {
        String[] arr = ip.split("\\.");
        double d1 = Double.parseDouble(arr[0]);
        double d2 = Double.parseDouble(arr[1]);
        double d3 = Double.parseDouble(arr[2]);
        double d4 = Double.parseDouble(arr[3]);
        return d1 * Math.pow(256, 3) + d2 * Math.pow(256, 2) + d3 * 256 + d4;
    }


}
