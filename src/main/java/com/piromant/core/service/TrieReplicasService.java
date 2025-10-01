package com.piromant.core.service;

import com.piromant.core.dal.TrieContainer;
import com.piromant.core.model.trie.TrieNode;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.NewReplicaEvent;
import com.piromant.dto.request.NewReplicaRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrieReplicasService {

    private final TrieContainer trieContainer;
    private final EventManager eventManager;

    public boolean addNewReplica(NewReplicaRequest newReplicaRequest) {//TODO not ddos-saved. Check sender. Check newReplica
        boolean success =
                this.trieContainer.addReplicaForPrefix(newReplicaRequest.prefix(), newReplicaRequest.address());
        if (!success) {
            return false;
        }
        this.eventManager.publish(new NewReplicaEvent(newReplicaRequest.prefix(), newReplicaRequest.address()));
        return true;
    }

    public TrieNode getTrieNodeForPrefix(String prefix) {//TODO implement. Who is asking, Ddos-save
        return this.trieContainer.getTrieNode(prefix);
    }

    public String hashedTrieNodeForPrefix(String prefix) { //TODO implement
        return "";
    }
}
