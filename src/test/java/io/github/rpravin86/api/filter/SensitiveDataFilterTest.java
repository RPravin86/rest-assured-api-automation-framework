package io.github.rpravin86.api.filter;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SensitiveDataFilterTest {

    @Test
    public void shouldProtectAuthenticationHeaders() {
        assertThat(SensitiveDataFilter.sensitiveHeaders())
                .contains(
                        "Authorization",
                        "Proxy-Authorization",
                        "X-API-Key",
                        "Cookie",
                        "Set-Cookie");
    }

    @Test
    public void shouldProtectSessionCookies() {
        assertThat(SensitiveDataFilter.sensitiveCookies())
                .contains("session", "sessionId", "JSESSIONID");
    }

    @Test
    public void shouldProtectCredentialParameters() {
        assertThat(SensitiveDataFilter.sensitiveParameters())
                .contains(
                        "access_token",
                        "refresh_token",
                        "api_key",
                        "client_secret",
                        "password",
                        "secret");
    }
}
