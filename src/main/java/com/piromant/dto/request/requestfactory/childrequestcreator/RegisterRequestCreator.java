package com.piromant.dto.request.requestfactory.childrequestcreator;

import com.piromant.dto.request.Redirectable;
import com.piromant.dto.request.RegisterRequest;

public class RegisterRequestCreator implements RequestForChildCreator {
    @Override
    public Redirectable createRequestForChild(Redirectable request) {
        RegisterRequest registerRequest = (RegisterRequest) request;
        return new RegisterRequest(registerRequest.address(), registerRequest.username(),
                registerRequest.currentLevel() + 1);
    }
}
