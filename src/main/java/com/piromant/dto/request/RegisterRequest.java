package com.piromant.dto.request;

public record RegisterRequest(String address, String username, Integer currentLevel) implements Redirectable {};
