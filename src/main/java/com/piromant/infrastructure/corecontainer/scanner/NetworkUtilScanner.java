package com.piromant.infrastructure.corecontainer.scanner;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.RedirectingClient;
import com.piromant.infrastructure.corecontainer.annotation.NetworkUtilImplementation;
import com.piromant.infrastructure.corecontainer.annotation.RedirectingClientImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class NetworkUtilScanner {

    public static List<NetworkUtil> findNetworkUtils() {
        List<NetworkUtil> networkUtils = new ArrayList<>();

        ServiceLoader<NetworkUtil> loader = ServiceLoader.load(NetworkUtil.class);
        for (NetworkUtil container : loader) {
            if (container.getClass().isAnnotationPresent(NetworkUtilImplementation.class)) {
                networkUtils.add(container);
            }
        }

        return networkUtils;

    }
}
