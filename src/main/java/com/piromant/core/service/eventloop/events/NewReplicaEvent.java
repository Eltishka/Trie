package com.piromant.core.service.eventloop.events;

import lombok.Data;

@Data
public class NewReplicaEvent implements Event {
    private final String prefix;
    private final String address;
}
