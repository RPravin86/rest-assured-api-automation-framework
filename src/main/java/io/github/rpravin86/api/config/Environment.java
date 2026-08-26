package io.github.rpravin86.api.config;

import java.util.Arrays;
import java.util.Locale;

/**
 * Supported execution environments and their matching configuration files.
 */
public enum Environment {

    DEV("dev"),
    QA("qa");

    private final String name;

    Environment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getConfigFile() {
        return "config/" + name + ".properties";
    }

    /**
     * Resolves an environment without making the caller handle letter casing.
     *
     * @param value environment name supplied through a system property or variable
     * @return matching supported environment
     * @throws IllegalArgumentException when the value is blank or unsupported
     */
    public static Environment from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Environment name must not be blank");
        }

        String normalizedValue = value.trim().toLowerCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(environment -> environment.name.equals(normalizedValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported environment '%s'. Supported environments: %s"
                                .formatted(value, supportedValues())));
    }

    private static String supportedValues() {
        return Arrays.stream(values())
                .map(Environment::getName)
                .toList()
                .toString();
    }
}
