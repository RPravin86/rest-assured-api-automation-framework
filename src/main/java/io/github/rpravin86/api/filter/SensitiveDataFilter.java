package io.github.rpravin86.api.filter;

import io.qameta.allure.restassured.AllureRestAssured;

import java.util.Set;

/**
 * Allure REST Assured filter configured to redact sensitive HTTP headers.
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

    public SensitiveDataFilter() {
        setRequestAttachmentName("HTTP request");
        setResponseAttachmentName("HTTP response");
    }

    public static Set<String> sensitiveHeaders() {
        return SENSITIVE_HEADERS;
    }

}
