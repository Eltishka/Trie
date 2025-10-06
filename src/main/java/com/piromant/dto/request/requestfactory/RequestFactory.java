package com.piromant.dto.request.requestfactory;

import com.piromant.dto.request.Redirectable;
import com.piromant.dto.request.requestfactory.childrequestcreator.RequestForChildCreator;

import java.util.HashMap;
import java.util.Map;

public class RequestFactory {

    private final Map<Class<Redirectable>, RequestForChildCreator> creators;
    public RequestFactory() {
        this.creators = new HashMap<>();
    }

    public Redirectable createRequestForChild(Redirectable request) {
        return this.creators.get(request.getClass()).createRequestForChild(request);
    }

    public void registerCreator(Class<Redirectable> requestClass, RequestForChildCreator creator) {
        this.creators.put(requestClass, creator);
    }
}
