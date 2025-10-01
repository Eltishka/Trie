package com.piromant.infrastructure.dal;

import com.piromant.core.dal.NetworkUtil;

public class DockerNetworkUtil implements NetworkUtil {

    @Override
    public String getMyAddress() {
        return System.getenv("USERNAME");
    }
}
