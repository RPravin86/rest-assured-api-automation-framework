package io.github.rpravin86.api.util;

import java.util.UUID;

/**
 * Generates unique, parallel-safe values for API test data.
 */
public final class RandomDataGenerator {

    private static final int UNIQUE_SUFFIX_LENGTH = 12;

    private RandomDataGenerator() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static String uniqueName() {
        return "API User " + uniqueSuffix();
    }

    public static String uniqueEmail() {
        return "api.user." + uniqueSuffix() + "@example.com";
    }

    private static String uniqueSuffix() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, UNIQUE_SUFFIX_LENGTH);
    }
}
