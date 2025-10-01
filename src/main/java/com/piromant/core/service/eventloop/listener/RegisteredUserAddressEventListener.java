package com.piromant.core.service.eventloop.listener;

import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieRegistry;
import com.piromant.core.service.eventloop.events.Event;
import com.piromant.core.service.eventloop.events.RegisteredUserAddressEvent;
import com.piromant.dto.request.RegisterRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisteredUserAddressEventListener implements EventListener {

    private final TrieRegistry trie;
    private final RedirectingClient redirectingClient;

    @Override
    public boolean canProcess(Event event) {
        return event instanceof RegisteredUserAddressEvent;
    }

    @Override
    public void process(Event event) { //TODO gossip
        RegisteredUserAddressEvent regEvent = (RegisteredUserAddressEvent) event;
        this.trie.getReplicasByPrefix(regEvent.getPrefix()).forEach( (el) ->
                this.redirectingClient.redirect(
                        new RegisterRequest(regEvent.getUserAddress(), regEvent.getPrefix(), regEvent.getPrefix().length()),
                        el
                )
        );
    }
}
