package com.piromant.core.service.eventloop.listener;

import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieRegistry;
import com.piromant.core.service.eventloop.events.Event;
import com.piromant.core.service.eventloop.events.NewChildEvent;
import com.piromant.dto.request.NewChildRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NewChildEventListener implements EventListener {

    private final TrieRegistry trie;
    private final RedirectingClient redirectingClient;

    @Override
    public boolean canProcess(Event event) {
        return event instanceof NewChildEvent;
    }

    @Override
    public void process(Event event) {
        NewChildEvent childEvent = (NewChildEvent) event;
        this.trie.getReplicasByPrefix(childEvent.getPrefix()).forEach( (el) ->
                this.redirectingClient.redirect(
                        new NewChildRequest(childEvent.getPrefix(), childEvent.getNext(), childEvent.getChildAddress()),
                        el
                )
        );
    }
}
