package com.piromant.core.service;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.RedirectingClient;
import com.piromant.core.dal.TrieContainer;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.NewChildEvent;
import com.piromant.core.service.eventloop.events.RegisteredUserAddressEvent;
import com.piromant.dto.request.NewChildRequest;
import com.piromant.dto.request.RegisterRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrieStructureService {

    private final TrieContainer trie;
    private final RedirectingClient redirectingClient;
    private final EventManager eventManager;
    private final NetworkUtil networkUtil;

    public Boolean register(RegisterRequest request) { //TODO multiple child redirecting
        String prefix = request.username().substring(0, request.currentLevel());
        if (!this.trie.containsPrefix(prefix)) {
            return false;
        }
        if(request.currentLevel() == request.username().length()) {
            this.trie.registerUserAddress(prefix, request.address());
            this.eventManager.publish(new RegisteredUserAddressEvent(prefix, request.address()));
            return true;
        }

        Character next = request.username().charAt(request.currentLevel());

        String address = this.trie.getRandomChildAddress(prefix, next);

        RegisterRequest newRequest = new RegisterRequest(request.address(), request.username(), request.currentLevel() + 1);

        if(address == null) {
            this.trie.createNewChild(prefix, next, this.networkUtil.getMyAddress());
            this.eventManager.publish(new NewChildEvent(prefix, next, this.networkUtil.getMyAddress()));
            return register(newRequest);
        }

        return (Boolean) this.redirectingClient.redirect(newRequest, address);
    }

    public void addNewChild(NewChildRequest request) { //TODO not ddos-saved. Check sender. Check child replica
        if (!this.trie.containsPrefix(request.prefix())) {
            return;
        }

        if(this.trie.containsPrefix(request.prefix() + request.next())) {
            return;
        }

        this.trie.createNewChild(request.prefix(), request.next(), request.address());
        this.eventManager.publish(new NewChildEvent(request.prefix(), request.next(), request.address()));
    }
}
