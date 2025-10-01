package com.piromant.core.service.eventloop.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisteredUserAddressEvent implements Event {
    private final String prefix;
    private final String userAddress;
}
