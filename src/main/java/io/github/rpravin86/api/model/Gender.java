package io.github.rpravin86.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Gender values accepted by the GoREST user API.
 */
public enum Gender {

    FEMALE("female"),
    MALE("male");

    private final String apiValue;

    Gender(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }

    @JsonCreator
    public static Gender fromApiValue(String value) {
        return Arrays.stream(values())
                .filter(gender -> gender.apiValue.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported gender value: " + value));
    }
}
