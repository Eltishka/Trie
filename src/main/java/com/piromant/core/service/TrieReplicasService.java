package com.piromant.core.service;

import com.piromant.core.dal.TrieContainer;
import com.piromant.core.model.trie.TrieNode;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.NewReplicaEvent;
import com.piromant.dto.request.GetTrieNodeRequest;
import com.piromant.dto.request.NewReplicaRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrieReplicasService {

    private final TrieContainer trieContainer;
    private final EventManager eventManager;

    public boolean addReplica(NewReplicaRequest newReplicaRequest) {

        String prefix = newReplicaRequest.prefix();

        if(!this.trieContainer.containsPrefix(prefix)) {
            return false;
        }

        if(this.trieContainer.getTrieNodeHash(prefix) != newReplicaRequest.hash()) {
            return false; //ask trienode for hash mb
        }

        boolean success =
                this.trieContainer.addReplicaForPrefix(newReplicaRequest.prefix(), newReplicaRequest.address());
        if (!success) {
            return false;
        }
        this.eventManager.publish(new NewReplicaEvent(newReplicaRequest.prefix(), newReplicaRequest.address()));
        return true;
    }

    public TrieNode getTrieNodeForPrefix(GetTrieNodeRequest getTrieNodeRequest) {
        String prefix = getTrieNodeRequest.prefix();

        if(prefix.isEmpty()) {
            return this.trieContainer.getTrieNode("");
        }

        if(!this.trieContainer.containsPrefix(prefix)) {
            return null;
        }

        if(this.trieContainer.getTrieNodeHash(prefix) != getTrieNodeRequest.hash()) {
            return null;
        }

        return this.trieContainer.getTrieNode(prefix);
    }

}
