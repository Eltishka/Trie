package com.piromant.core.service.eventloop;

import com.piromant.core.service.eventloop.events.Event;
import com.piromant.core.service.eventloop.listener.EventListener;

import java.util.LinkedList;
import java.util.List;

public class EventManagerSimpleImpl implements EventManager {

    private final List<EventListener> listeners;

    public EventManagerSimpleImpl() {
        this.listeners = new LinkedList<>();
    }

    @Override
    public void subscribe(EventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void unsubscribe(EventListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void publish(Event event) {
        for (EventListener listener : this.listeners) {
            if(!listener.canProcess(event)) {
                continue;
            }
            listener.process(event);
        }
    }
}
