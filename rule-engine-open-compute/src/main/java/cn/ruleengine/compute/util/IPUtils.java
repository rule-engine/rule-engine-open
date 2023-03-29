/*
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.compute.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @date 2019/8/14
 * @since 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IPUtils {

    /**
     * 服务端的ip地址
     */
    public static final String SERVER_IP = getServerIp();

    /**
     * 获取请求IP地址
     *
     * @return 请求的ip地址
     */
    public static String getRequestIp() {
        HttpServletRequest request = HttpServletUtils.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取服务端ip
     *
     * @return 服务端ip地址
     */
    private static String getServerIp() {
        String clientIp = StringPool.EMPTY;
        //根据网卡取本机配置的IP,定义网络接口枚举类
        Enumeration<NetworkInterface> allNetInterfaces;
        try {
            //获得网络接口
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            //声明一个InetAddress类型ip地址
            InetAddress ip;
            //遍历所有的网络接口
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                //同样再定义网络地址枚举类
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    //InetAddress类包括Inet4Address和Inet6Address
                    if ((ip instanceof Inet4Address)) {
                        if (!"127.0.0.1".equals(ip.getHostAddress())) {
                            clientIp = ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("SocketException", e);
        }
        return clientIp;
    }

}
