package com.piromant.infrastructure.controller;

import com.piromant.core.service.TrieStructureService;
import com.piromant.dto.request.NewChildRequest;
import com.piromant.dto.request.RegisterRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StructureRequestsHandler implements Handler {

    private final TrieStructureService trieStructureService;


    @Override
    public boolean canHandle(Object request) {
        return request instanceof RegisterRequest || request instanceof NewChildRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof RegisterRequest) {
            return trieStructureService.register((RegisterRequest) request);
        }
        this.trieStructureService.addNewChild((NewChildRequest) request);
        return null;
    }
}
