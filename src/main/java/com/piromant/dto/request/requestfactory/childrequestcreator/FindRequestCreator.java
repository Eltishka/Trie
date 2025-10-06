package com.piromant.dto.request.requestfactory.childrequestcreator;

import com.piromant.dto.request.FindRequest;
import com.piromant.dto.request.Redirectable;

public class FindRequestCreator implements RequestForChildCreator {
    @Override
    public Redirectable createRequestForChild(Redirectable request) {
        FindRequest findRequest = (FindRequest) request;
        return new FindRequest(findRequest.username(), findRequest.currentLevel() + 1);
    }
}
