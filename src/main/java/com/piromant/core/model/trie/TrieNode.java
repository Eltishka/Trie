package com.piromant.core.model.trie;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public final class TrieNode implements Serializable {

    @Getter
    private final String userAddress;

    private final List<String> replicas;
    private final Map<Character, List<String>> children;

    public TrieNode(String userAddress) {
        this.userAddress = userAddress;
        this.replicas = new ArrayList<>();
        this.children = new HashMap<>();
    }

    public TrieNode(String userAddress, List<String> replicas, Map<Character, List<String>> children) {
        this.userAddress = userAddress;
        this.replicas = replicas;
        this.children = children;
    }

    public List<String> getReplicas() {
        return new ArrayList<>(this.replicas);
    }

    public Map<Character, List<String>> getChildren() {
        return this.children.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String toString() {
        return "TrieNode{" +
                "userAddress='" + userAddress + '\'' +
                ", replicas=" + replicas +
                ", children=" + children +
                '}';
    }
}
