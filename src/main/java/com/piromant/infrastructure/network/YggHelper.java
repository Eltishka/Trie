package com.piromant.infrastructure.network;

import com.piromant.core.dal.NetworkUtil;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class YggHelper implements NetworkUtil {

    @Override
    public String getMyAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                if (!iface.isUp() || iface.isLoopback()) continue;

                for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
                    InetAddress inetAddr = addr.getAddress();

                    if (inetAddr instanceof Inet6Address) {
                        String ip = inetAddr.getHostAddress();

                        // отрезаем zone-id (%ygg0)
                        int zoneIndex = ip.indexOf('%');
                        if (zoneIndex != -1) {
                            ip = ip.substring(0, zoneIndex);
                        }

                        // Yggdrasil работает в диапазоне 0200::/7
                        if (ip.startsWith("0200") || ip.toLowerCase().startsWith("200:")) {
                            return inetAddr.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить Yggdrasil-адрес", e);
        }
        return null;
    }
}
