package com.piromant.dto.request;

public record FindRequest(String username, Integer currentLevel) implements Redirectable {}

