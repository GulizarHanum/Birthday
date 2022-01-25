package com.example.api.students.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Role {

    FRIEND("FRIEND"), //друг
    FAMILIAR("FAMILIAR"), // знакомый
    COWORKER("COWORKER"), // коллега
    FAMILY("FAMILY"); //родственник

    private String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Получить значение енума из строки
     *
     * @param text строковое представление значения енума (напр. "FRIEND")
     *
     * @return значение енума (Role.FRIEND)
     */
    @JsonCreator
    public static Role fromValue(String text) {
        return Arrays.stream(Role.values())
                .filter(candidate -> candidate.value.equals(text))
                .findFirst()
                .orElse(null);
    }
}
