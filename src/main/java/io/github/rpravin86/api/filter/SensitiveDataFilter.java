package io.github.rpravin86.api.filter;

import io.qameta.allure.restassured.AllureRestAssured;

import java.util.Set;

/**
 * Allure REST Assured filter configured to redact sensitive HTTP data.
 *
 * <p>Redaction is applied only to the Allure HTTP-exchange attachment. The
 * original REST Assured request remains unchanged and reaches the API with its
 * real authentication values.</p>
 */
public final class SensitiveDataFilter extends AllureRestAssured {

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "Authorization",
            "Proxy-Authorization",
            "X-API-Key",
            "API-Key",
            "Cookie",
            "Set-Cookie");

    private static final Set<String> SENSITIVE_COOKIES = Set.of(
            "session",
            "sessionId",
            "JSESSIONID");

    private static final Set<String> SENSITIVE_PARAMETERS = Set.of(
            "access_token",
            "refresh_token",
            "api_key",
            "client_secret",
            "token",
            "password",
            "secret");

    public SensitiveDataFilter() {
        setAttachmentName("HTTP exchange");
        configureHttpExchange(exchange -> exchange
                .redactHeaders(SENSITIVE_HEADERS)
                .redactCookies(SENSITIVE_COOKIES)
                .redactQueryParameters(SENSITIVE_PARAMETERS)
                .redactFormParameters(SENSITIVE_PARAMETERS));
    }

    public static Set<String> sensitiveHeaders() {
        return SENSITIVE_HEADERS;
    }

    static Set<String> sensitiveCookies() {
        return SENSITIVE_COOKIES;
    }

    static Set<String> sensitiveParameters() {
        return SENSITIVE_PARAMETERS;
    }
}
