package io.github.rpravin86.api.specification;

import io.github.rpravin86.api.config.ConfigManager;
import io.github.rpravin86.api.constants.FrameworkConstants;
import io.github.rpravin86.api.filter.SensitiveDataFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Creates fresh REST Assured request specifications for GoREST calls.
 */
public final class RequestSpecFactory {

    private RequestSpecFactory() {
        throw new IllegalStateException("Factory class cannot be instantiated");
    }

    /**
     * Builds the standard specification used by authenticated endpoints.
     *
     * @return a new request specification containing common configuration
     */
    public static RequestSpecification authenticatedRequestSpec() {
        return commonRequestSpecBuilder()
                .addHeader(
                        FrameworkConstants.AUTHORIZATION_HEADER,
                        FrameworkConstants.BEARER_PREFIX + ConfigManager.getApiToken())
                .build();
    }

    /**
     * Builds a specification without credentials for public and
     * authentication-negative scenarios.
     *
     * @return a new unauthenticated request specification
     */
    public static RequestSpecification publicRequestSpec() {
        return commonRequestSpecBuilder().build();
    }

    private static RequestSpecBuilder commonRequestSpecBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setBasePath(ConfigManager.getBasePath())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new SensitiveDataFilter())
                .setConfig(httpClientConfig());
    }

    private static RestAssuredConfig httpClientConfig() {
        return RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(
                                "http.connection.timeout",
                                ConfigManager.getConnectionTimeoutMs())
                        .setParam(
                                "http.socket.timeout",
                                ConfigManager.getResponseTimeoutMs()))
                .logConfig(LogConfig.logConfig()
                        .blacklistHeaders(
                                SensitiveDataFilter.sensitiveHeaders()));
    }
}
