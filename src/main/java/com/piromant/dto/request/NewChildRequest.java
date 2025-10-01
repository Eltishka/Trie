package com.piromant.dto.request;

public record NewChildRequest(String prefix, Character next, String address) implements Redirectable {

}
