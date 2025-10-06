package com.piromant.core.service;

import com.piromant.dto.response.Response;
import com.sun.net.httpserver.Request;

public interface TrieService {

    Response process(Request request);
}
