package com.piromant.core.model.trie;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public final class TrieNode implements Serializable {

    @Getter
    private final String userAddress;
    @Getter
    private final Integer parentMinHash;
    @Getter
    private final Integer hash;

    private final List<String> replicas;
    private final Map<Character, List<String>> children;

    public TrieNode(String userAddress, Integer parentMinHash, Integer hash) {
        this.userAddress = userAddress;
        this.parentMinHash = parentMinHash;
        this.hash = hash;
        this.replicas = new ArrayList<>();
        this.children = new HashMap<>();
    }

    public TrieNode(String userAddress, Integer parentMinHash, Integer hash, List<String> replicas, Map<Character, List<String>> children) {
        this.userAddress = userAddress;
        this.parentMinHash = parentMinHash;
        this.hash = hash;
        this.replicas = replicas;
        this.children = children;
    }

    public List<String> getReplicas() {
        return new ArrayList<>(this.replicas);
    }

    public Map<Character, List<String>> getChildren() {
        return this.children.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int getMinHash() {
        return Objects.hash(this.parentMinHash, this.userAddress, this.children.keySet());
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
