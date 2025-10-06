package com.piromant.core.dal;

public interface TrieContainer extends TrieRegistry {

    boolean registerUserAddress(String prefix, String address);
    boolean createNewChild(String prefix, Character character, String childAddress, int parentHash);
    boolean addReplicaForPrefix(String prefix, String address);

}
