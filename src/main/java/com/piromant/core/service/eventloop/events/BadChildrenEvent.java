package com.piromant.core.service.eventloop.events;

import com.piromant.dto.request.Redirectable;
import com.piromant.dto.response.Response;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BadChildrenEvent implements Event { //implements listeners, service processing etc

    private final Redirectable request;
    private final Response trueResult;
    private final List<Response> badChildren;
}
