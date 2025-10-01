package com.piromant.infrastructure.controller;

import com.piromant.core.model.trie.TrieNode;
import com.piromant.core.service.TrieReplicasService;
import com.piromant.core.service.TrieStructureService;
import com.piromant.dto.request.StoleNodeRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetTrieNodeHandler implements Handler {

    private final TrieReplicasService trieReplicasService;
    
    @Override
    public boolean canHandle(Object request) {
        return request instanceof StoleNodeRequest;
    }

    @Override
    public Object handle(Object request) {
        System.out.println("Get trie node + " + request);
        return this.trieReplicasService.getTrieNodeForPrefix(((StoleNodeRequest) request).prefix());
    }
}
