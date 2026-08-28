package io.github.rpravin86.api.constants;

/**
 * Shared framework values that are not environment-specific.
 */
public final class FrameworkConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_SCHEMA = "schemas/user-schema.json";
    public static final String USERS_SCHEMA = "schemas/users-schema.json";
    public static final String ERROR_SCHEMA = "schemas/error-schema.json";

    private FrameworkConstants() {
        throw new IllegalStateException("Constants class cannot be instantiated");
    }
}
