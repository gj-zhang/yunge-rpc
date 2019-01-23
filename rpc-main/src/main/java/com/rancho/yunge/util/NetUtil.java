package com.rancho.yunge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class NetUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetUtil.class);

    private static InetAddress LOCAL_ADDRESS = null;

    public static String getIpAndPort(String ip, int port) {
        if (ip == null) {
            return null;
        }
        return ip + ":" + String.valueOf(port);
    }

    public static boolean isPortUsed(int port) {
        boolean used;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            used = false;
        } catch (IOException e) {
            logger.info(" bind port[{}] is in use.", port);
            used = true;
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ignore) {
                }
            }
        }
        return used;
    }

    public static String getLocalAddress() {
        return getLocalAddress0().getHostAddress();
    }

    private synchronized static InetAddress getLocalAddress0() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress local = getLocalAddress1();
        LOCAL_ADDRESS = local;
        return local;
    }

    private static InetAddress getLocalAddress1() {
        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        if (inetAddress.isLoopbackAddress()) {
                            continue;
                        }
                        if (inetAddress.isSiteLocalAddress()) {
                            return inetAddress;
                        } else {
                            local = inetAddress;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e.getMessage(), e);
        }

        return local;
    }

    public static Object[] parseIpPort(String address) {
        String[] array = address.split(":");

        String host = array[0];
        int port = Integer.parseInt(array[1]);

        return new Object[]{host, port};
    }
}
