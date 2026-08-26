package io.github.rpravin86.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central access point for environment configuration and runtime secrets.
 *
 * <p>Non-secret configuration is loaded once from the selected classpath
 * properties file. JVM system properties can override individual values.
 * Secrets are resolved only at the point of use and are never stored in a
 * project resource file.</p>
 */
public final class ConfigManager {

    static final String TEST_ENVIRONMENT_PROPERTY = "test.environment";
    static final String TEST_ENVIRONMENT_VARIABLE = "TEST_ENVIRONMENT";
    static final String API_TOKEN_PROPERTY = "gorest.api.token";
    static final String API_TOKEN_VARIABLE = "GOREST_API_TOKEN";

    private static final Environment DEFAULT_ENVIRONMENT = Environment.QA;

    private static final String BASE_URL = "base.url";
    private static final String BASE_PATH = "base.path";
    private static final String CONNECTION_TIMEOUT = "connection.timeout.ms";
    private static final String RESPONSE_TIMEOUT = "response.timeout.ms";

    private final Environment environment;
    private final Properties properties;

    private ConfigManager() {
        environment = resolveEnvironment();
        properties = loadProperties(environment);
        validateConfiguration();
    }

    private static final class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    private static ConfigManager instance() {
        return Holder.INSTANCE;
    }

    public static Environment getEnvironment() {
        return instance().environment;
    }

    public static String getBaseUrl() {
        return instance().getRequiredProperty(BASE_URL);
    }

    public static String getBasePath() {
        return instance().getRequiredProperty(BASE_PATH);
    }

    public static int getConnectionTimeoutMs() {
        return instance().getPositiveInteger(CONNECTION_TIMEOUT);
    }

    public static int getResponseTimeoutMs() {
        return instance().getPositiveInteger(RESPONSE_TIMEOUT);
    }

    /**
     * Resolves the GoREST token using this precedence:
     * JVM system property, then operating-system environment variable.
     *
     * @return API token without a {@code Bearer} prefix
     * @throws IllegalStateException when no token has been configured
     */
    public static String getApiToken() {
        String token = firstNonBlank(
                System.getProperty(API_TOKEN_PROPERTY),
                System.getenv(API_TOKEN_VARIABLE));

        if (token == null) {
            throw new IllegalStateException(
                    "GoREST API token is missing. Set -D%s or the %s environment variable."
                            .formatted(API_TOKEN_PROPERTY, API_TOKEN_VARIABLE));
        }

        return token;
    }

    private static Environment resolveEnvironment() {
        String selectedEnvironment = firstNonBlank(
                System.getProperty(TEST_ENVIRONMENT_PROPERTY),
                System.getenv(TEST_ENVIRONMENT_VARIABLE));

        return selectedEnvironment == null
                ? DEFAULT_ENVIRONMENT
                : Environment.from(selectedEnvironment);
    }

    private static Properties loadProperties(Environment environment) {
        Properties loadedProperties = new Properties();
        String resourcePath = environment.getConfigFile();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = ConfigManager.class.getClassLoader();
        }

        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException(
                        "Configuration file was not found on the classpath: " + resourcePath);
            }

            loadedProperties.load(inputStream);
            return loadedProperties;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load configuration file: " + resourcePath,
                    exception);
        }
    }

    private String getRequiredProperty(String key) {
        String value = firstNonBlank(
                System.getProperty(key),
                properties.getProperty(key));

        if (value == null) {
            throw new IllegalStateException(
                    "Required configuration property is missing: " + key);
        }

        return value;
    }

    private int getPositiveInteger(String key) {
        String value = getRequiredProperty(key);

        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue <= 0) {
                throw new IllegalStateException(
                        "Configuration property must be greater than zero: " + key);
            }
            return parsedValue;
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "Configuration property must be a valid integer: " + key,
                    exception);
        }
    }

    private void validateConfiguration() {
        getRequiredProperty(BASE_URL);
        getRequiredProperty(BASE_PATH);
        getPositiveInteger(CONNECTION_TIMEOUT);
        getPositiveInteger(RESPONSE_TIMEOUT);
    }

    private static String firstNonBlank(String firstValue, String secondValue) {
        if (firstValue != null && !firstValue.isBlank()) {
            return firstValue.trim();
        }
        if (secondValue != null && !secondValue.isBlank()) {
            return secondValue.trim();
        }
        return null;
    }
}
