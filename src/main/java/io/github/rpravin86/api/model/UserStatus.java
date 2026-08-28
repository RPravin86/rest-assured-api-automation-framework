package io.github.rpravin86.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Status values accepted by the GoREST user API.
 */
public enum UserStatus {

    ACTIVE("active"),
    INACTIVE("inactive");

    private final String apiValue;

    UserStatus(String apiValue) {
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getApiValue() {
        return apiValue;
    }

    @JsonCreator
    public static UserStatus fromApiValue(String value) {
        return Arrays.stream(values())
                .filter(status -> status.apiValue.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported user status value: " + value));
    }
}
