package com.piromant.infrastructure.controller;

import com.piromant.core.service.TrieSearchingService;
import com.piromant.dto.request.FindRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindRequestHandler implements Handler{

    private final TrieSearchingService trieService;

    @Override
    public boolean canHandle(Object request) {
        return request instanceof FindRequest;
    }

    @Override
    public Object handle(Object request) {
        return this.trieService.findUser((FindRequest) request);
    }
}
