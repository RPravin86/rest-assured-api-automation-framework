package io.github.rpravin86.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

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
}
