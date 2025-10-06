package com.piromant.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record RegisteredResponse(Boolean registered, List<String> path) implements Comparable<RegisteredResponse>, Response {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredResponse that = (RegisteredResponse) o;
        return Objects.equals(registered, that.registered);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(registered);
    }

    @Override
    public int compareTo(RegisteredResponse o) {
        return Comparator.nullsFirst(Boolean::compareTo).compare(this.registered, o.registered);
    }

    @Override
    public List<String> path() {
        return new ArrayList<>(path);
    }
}
