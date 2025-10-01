package com.piromant.core.service.eventloop.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NewChildEvent implements Event{

    private final String prefix;
    private final Character next;
    private final String childAddress;
}
