package com.piromant.infrastructure.dal;

import com.piromant.core.dal.TrieContainer;
import com.piromant.core.model.trie.TrieNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrieContainerMapImpl implements TrieContainer {

    private final Map<String, TrieNode> prefixToNode;

    public TrieContainerMapImpl() {
        this.prefixToNode = new HashMap<>();
        this.prefixToNode.put("", new TrieNode(null));
    }

    @Override
    public boolean registerUserAddress(String prefix, String address) {
        if (!containsPrefix(prefix)) {
            return false;
        }
        if (this.prefixToNode.get(prefix).getUserAddress() != null) {
            return false;
        }
        TrieNode newOne = new TrieNode(address, this.prefixToNode.get(prefix).getReplicas(), this.prefixToNode.get(prefix).getChildren());
        this.prefixToNode.put(prefix, newOne);
        return true;
    }

    @Override
    public boolean createNewChild(String prefix, Character character, String childAddress) {
        if (!containsPrefix(prefix)) {
            return false;
        }
        TrieNode prev = this.prefixToNode.get(prefix);
        Map<Character, List<String>> children = this.prefixToNode.get(prefix).getChildren();
        if (!children.containsKey(character)) {
            children.put(character, new ArrayList<>());
        }
        List<String> childReplicas = children.get(character);
        if(childReplicas.contains(childAddress)) {
            return false;
        }
        this.prefixToNode.put(prefix, new TrieNode(prev.getUserAddress(), prev.getReplicas(), children));
        childReplicas.add(childAddress);
        TrieNode newNode = new TrieNode(prev.getUserAddress(), prev.getReplicas(), children);
        this.prefixToNode.put(prefix + character, newNode);
        this.prefixToNode.put(prefix + character, new TrieNode(null));
        return true;
    }

    @Override
    public boolean addReplicaForPrefix(String prefix, String address) {
        if (!containsPrefix(prefix)) {
            return false;
        }
        TrieNode prev = this.prefixToNode.get(prefix);
        List<String> replicas = this.prefixToNode.get(prefix).getReplicas();
        replicas.add(address);
        TrieNode newNode = new TrieNode(prev.getUserAddress(), replicas, prev.getChildren());
        this.prefixToNode.put(prefix, newNode);
        return true;
    }

    @Override
    public boolean containsPrefix(String prefix) {
        return prefixToNode.containsKey(prefix);
    }

    @Override
    public String getUserAddress(String prefix) {
        if (!containsPrefix(prefix)) {
            return null;
        }
        return this.prefixToNode.get(prefix).getUserAddress();

    }

    @Override
    public String getRandomChildAddress(String prefix, Character character) {

        if (!containsPrefix(prefix)) {
            return null;
        }
        if(!this.prefixToNode.get(prefix).getChildren().containsKey(character)) {
            return null;
        }

        return this.prefixToNode.get(prefix).getChildren().get(character).getLast();

    }

    @Override
    public List<String> getReplicasByPrefix(String prefix) {
        return this.prefixToNode.get(prefix).getReplicas();
    }

    @Override
    public TrieNode getTrieNode(String prefix) {
        return this.prefixToNode.get(prefix);
    }
}
