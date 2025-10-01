package com.piromant.infrastructure.corecontainer;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieContainer;
import com.piromant.core.service.TrieReplicasService;
import com.piromant.core.service.TrieSearchingService;
import com.piromant.core.service.TrieStructureService;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.EventManagerSimpleImpl;
import com.piromant.core.service.eventloop.listener.NewChildEventListener;
import com.piromant.core.service.eventloop.listener.NewReplicaEventListener;
import com.piromant.core.service.eventloop.listener.RegisteredUserAddressEventListener;
import lombok.Getter;

@Getter
public class CoreContainer {

    private final TrieSearchingService trieSearchingService;
    private final TrieStructureService trieStructureService;
    private final TrieReplicasService trieReplicasService;

    public CoreContainer(TrieContainer container, RedirectingClient redirectingClient, NetworkUtil networkUtil, int redirectingRootCount) {
        EventManager eventManager = new EventManagerSimpleImpl();
        eventManager.subscribe(new NewChildEventListener(container, redirectingClient));
        eventManager.subscribe(new NewReplicaEventListener(container, redirectingClient));
        eventManager.subscribe(new RegisteredUserAddressEventListener(container, redirectingClient));
        this.trieReplicasService = new TrieReplicasService(container, eventManager);
        this.trieStructureService = new TrieStructureService(container,redirectingClient, eventManager, networkUtil);
        this.trieSearchingService = new TrieSearchingService(container, redirectingClient, eventManager, networkUtil, redirectingRootCount);

    }
}
