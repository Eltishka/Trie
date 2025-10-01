package com.piromant.infrastructure.network.server;

import com.piromant.infrastructure.controller.Handler;

import java.util.ArrayList;
import java.util.List;

public class HandlerInvoker {

    private final List<Handler> handlers;

    public HandlerInvoker(List<Handler> handlers) {
        this.handlers = handlers;

    }

    public Object invokeHandler(Object request) {
        for (Handler handler : handlers) {
            if(handler.canHandle(request)) {
                return handler.handle(request);
            }
        }
        return null;
    }
}
