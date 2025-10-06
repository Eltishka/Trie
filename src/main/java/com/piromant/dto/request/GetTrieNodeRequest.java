package com.piromant.dto.request;

public record GetTrieNodeRequest(String prefix, Integer hash) implements Redirectable {
}
