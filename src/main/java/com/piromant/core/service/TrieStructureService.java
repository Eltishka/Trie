package com.piromant.core.service;

import com.piromant.core.dal.NetworkUtil;
import com.piromant.core.dal.TrieContainer;
import com.piromant.core.service.eventloop.EventManager;
import com.piromant.core.service.eventloop.events.NewChildEvent;
import com.piromant.core.service.eventloop.events.RegisteredUserAddressEvent;
import com.piromant.dto.request.NewChildRequest;
import com.piromant.dto.request.RegisterRequest;
import com.piromant.dto.response.RegisteredResponse;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TrieStructureService {

    private final TrieContainer trie;
    private final ChildAskingService childAskingService;
    private final EventManager eventManager;
    private final NetworkUtil networkUtil;

    public RegisteredResponse register(RegisterRequest request, String senderAddress) {
        String prefix = request.username().substring(0, request.currentLevel());
        if (!this.trie.containsPrefix(prefix)) {
            return new RegisteredResponse(false, List.of(this.networkUtil.getMyAddress()));
        }

        if (!senderAddress.equals(request.address()) &&
                !request.levelHash().equals(this.trie.getTrieNodeHash(prefix))) {
            return new RegisteredResponse(false, List.of(this.networkUtil.getMyAddress()));
        }

        if (request.currentLevel() == request.username().length()) {
            String innerUserAddress = this.trie.getUserAddress(prefix);
            if (innerUserAddress == null) {
                this.trie.registerUserAddress(prefix, request.address());
                this.eventManager.publish(new RegisteredUserAddressEvent(prefix, request.address()));
            } else if (!innerUserAddress.equals(request.address())) {
                return new RegisteredResponse(false, List.of(this.networkUtil.getMyAddress()));
            }
            return new RegisteredResponse(true, List.of(this.networkUtil.getMyAddress()));
        }

        Character next = request.username().charAt(request.currentLevel());

        String address = this.trie.getRandomChildAddress(prefix, next);

        RegisterRequest newRequest = new RegisterRequest(request.address(), request.username(),
                request.currentLevel() + 1, this.trie.getChildHash(prefix, next));

        if (address == null) {
            this.trie.createNewChild(
                    prefix, next, this.networkUtil.getMyAddress(), this.trie.getTrieNodeHash(prefix));
            this.eventManager.publish(new NewChildEvent(
                    prefix, next, this.networkUtil.getMyAddress(), this.trie.getTrieNodeHash(prefix)));
            RegisteredResponse prevResponse = register(newRequest, senderAddress);
            return this.modifyResponse(prevResponse);
        }

        RegisteredResponse childResult;
        if (request.currentLevel() == 0) {
            childResult = (RegisteredResponse) this.childAskingService.getQuorumResponse(
                    request, prefix, next
            );
        } else {
            childResult = (RegisteredResponse) this.childAskingService.getSingleResponse(
                    request, prefix, next
            );
        }

        return this.modifyResponse(childResult);
    }

    public void addNewChild(NewChildRequest request, String senderAddress) {

        if (!this.trie.containsPrefix(request.prefix())) {
            return;
        }

        if (!this.trie.containsReplica(request.prefix(), senderAddress)) {
            return;
        }

        if (this.trie.containsPrefix(request.prefix() + request.next())) {
            return; //TODO maybe bad replica????
        }

        if (this.trie.getTrieNodeHash(request.prefix()) != request.parentHash()) {
            return; //TODO maybe bad replica????
        }

        this.trie.createNewChild(request.prefix(), request.next(), request.address(), request.parentHash());
        this.eventManager.publish(
                new NewChildEvent(request.prefix(), request.next(), request.address(), request.parentHash()));
    }

    private RegisteredResponse modifyResponse(RegisteredResponse response) {
        List<String> path = response.path();
        path.addFirst(this.networkUtil.getMyAddress());
        return new RegisteredResponse(response.registered(), path);
    }
}
