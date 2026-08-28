package io.github.rpravin86.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

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
}
