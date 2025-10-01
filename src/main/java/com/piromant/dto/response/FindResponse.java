package com.piromant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record FindResponse(String result, List<String> path) implements Comparable<FindResponse>, Serializable {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FindResponse that = (FindResponse) o;
        return Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(result);
    }

    @Override
    public int compareTo(FindResponse o) {
        return Comparator.nullsFirst(String::compareTo).compare(this.result, o.result);
    }

    @Override
    public List<String> path() {
        return new ArrayList<>(path);
    }
}
