package com.piromant.dto.request;

public record NewReplicaRequest(String prefix, String address) implements Redirectable {
}
