package com.piromant.dto.request.requestfactory.childrequestcreator;

import com.piromant.dto.request.Redirectable;

public interface RequestForChildCreator {

    Redirectable createRequestForChild(Redirectable request);
}
