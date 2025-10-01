package com.piromant.core.service.eventloop.listener;

import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieRegistry;
import com.piromant.core.service.eventloop.events.Event;
import com.piromant.core.service.eventloop.events.NewReplicaEvent;
import com.piromant.dto.request.NewReplicaRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NewReplicaEventListener implements EventListener {

    private final TrieRegistry trie;
    private final RedirectingClient redirectingClient;


    @Override
    public boolean canProcess(Event event) {
        return event instanceof NewReplicaEvent;
    }

    @Override
    public void process(Event event) {
        NewReplicaEvent replicaEvent = (NewReplicaEvent) event;
        this.trie.getReplicasByPrefix(replicaEvent.getPrefix()).forEach( (el) ->
                this.redirectingClient.redirect(
                        new NewReplicaRequest(replicaEvent.getPrefix(), replicaEvent.getAddress()),
                        el
                )
        );
    }
}
