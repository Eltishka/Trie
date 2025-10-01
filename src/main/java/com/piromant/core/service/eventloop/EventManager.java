package com.piromant.core.service.eventloop;

import com.piromant.core.service.eventloop.events.Event;
import com.piromant.core.service.eventloop.listener.EventListener;

public interface EventManager {
    void subscribe(EventListener listener);
    void unsubscribe(EventListener listener);
    void publish(Event event);
}
