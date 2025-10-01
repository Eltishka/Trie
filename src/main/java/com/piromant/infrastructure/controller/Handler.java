package com.piromant.infrastructure.controller;

public interface Handler {
    boolean canHandle(Object request);
    Object handle(Object request);
}
