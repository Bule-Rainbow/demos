package com.jwt.demo.util.ip;

import cn.hutool.core.net.Ipv4Util;
import com.googlecode.ipv6.IPv6Network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

/**
 * @author jiangwentao
 * @description ip测试
 * @createTime 2022年09月29日 16:32
 */
public class IPTest {
    public static void main(String[] args) throws Exception {
        //根据ipv4 CIDR 获取所有可用IP（isAll=false）
        List<String> ipList = Ipv4Util.list("10.10.10.1", 24, false);
        //根据ipv6 CIDR 获取起止ip等
        IPv6Network network = IPv6Network.fromString("2001:bca8::1/64");


        //获取本机IP
        String localhostIpAddress = IPUtil.getLocalhostIpAddress();

        //IPV4地址转换成Long类型数字
        Long aLong = IPUtil.transIpv4ToLong(localhostIpAddress);

        //long类型数字转成IPV4地址
        String ip = IPUtil.transLongToIPV4(aLong);

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

        System.out.println(sb.toString().toUpperCase());

    }
}
