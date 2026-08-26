package io.github.rpravin86.api.constants;

/**
 * Relative paths for GoREST resources.
 *
 * <p>The base URL and API version path remain environment configuration;
 * this class contains only resource-specific routes.</p>
 */
public final class ApiRoutes {

    public static final String USERS = "/users";

    private ApiRoutes() {
        throw new IllegalStateException("Constants class cannot be instantiated");
    }

    public static String userById(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
        return USERS + "/" + userId;
    }
}
