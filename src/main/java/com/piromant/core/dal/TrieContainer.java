package com.piromant.core.dal;

public interface TrieContainer extends TrieRegistry {

    boolean registerUserAddress(String prefix, String address);
    boolean createNewChild(String prefix, Character character, String childAddress);
    boolean addReplicaForPrefix(String prefix, String address);
}
