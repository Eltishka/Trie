package com.piromant.infrastructure.controller;

import com.piromant.core.service.TrieReplicasService;
import com.piromant.dto.request.NewReplicaRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NewReplicaRequestHandler implements Handler {

    private final TrieReplicasService replicasService;

    @Override
    public boolean canHandle(Object request) {
        return request instanceof NewReplicaRequest;
    }

    @Override
    public Object handle(Object request) {
        return replicasService.process((NewReplicaRequest) request);
    }
}
