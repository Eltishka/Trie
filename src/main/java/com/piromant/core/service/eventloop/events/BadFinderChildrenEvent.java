package com.piromant.core.service.eventloop.events;

import com.piromant.dto.request.Redirectable;
import com.piromant.dto.response.FindResponse;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BadFinderChildrenEvent implements Event {

    private final Redirectable request;
    private final String trueResult;
    private final List<FindResponse> badChildren;
}
