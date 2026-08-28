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

}
