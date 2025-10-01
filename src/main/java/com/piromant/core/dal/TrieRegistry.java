package com.piromant.core.dal;

import com.piromant.core.model.trie.TrieNode;

import java.util.List;

public interface TrieRegistry {

    boolean containsPrefix(String prefix);
    String getUserAddress(String prefix);
    String getRandomChildAddress(String prefix, Character character);
    List<String> getReplicasByPrefix(String prefix);
    TrieNode getTrieNode(String prefix);
}
