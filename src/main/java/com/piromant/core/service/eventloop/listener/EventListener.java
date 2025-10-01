package com.piromant.core.service.eventloop.listener;

import com.piromant.core.service.eventloop.events.Event;

public interface EventListener {
    boolean canProcess(Event event);
    void process(Event event);
}
