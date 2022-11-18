package com.jwt.demo.util.net;

import java.net.InetAddress;

/**
 * @author jiangwentao
 * @description
 * @createTime 2022年11月18日 13:30
 */
public class NetUtil {

    /**
     * 判断主机是否可访问
     *
     * @param hostIp  主机Ip
     * @param timeOut 超时时间
     * @return true/false 可访问/不可访问
     */
    public static Boolean isConnectable(String hostIp, int timeOut) {
        try {
            return InetAddress.getByName(hostIp).isReachable(timeOut);
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String hostIp = "192.168.122.63";
        Boolean connectable = isConnectable(hostIp, 3000);
        System.out.println("result:" + connectable);
    }
}
