package io.github.rpravin86.api.config;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigManagerTest {

    @Test
    public void shouldLoadRequiredConfiguration() {
        assertThat(ConfigManager.getBaseUrl()).isNotBlank();
        assertThat(ConfigManager.getBasePath()).startsWith("/");
        assertThat(ConfigManager.getConnectionTimeoutMs()).isPositive();
        assertThat(ConfigManager.getResponseTimeoutMs()).isPositive();
    }

    @Test
    public void shouldPreferSystemPropertyForApiToken() {
        String originalValue = System.getProperty(ConfigManager.API_TOKEN_PROPERTY);

        try {
            System.setProperty(ConfigManager.API_TOKEN_PROPERTY, "temporary-test-token");

            assertThat(ConfigManager.getApiToken())
                    .isEqualTo("temporary-test-token");
        } finally {
            restoreSystemProperty(ConfigManager.API_TOKEN_PROPERTY, originalValue);
        }
    }

    private void restoreSystemProperty(String key, String originalValue) {
        if (originalValue == null) {
            System.clearProperty(key);
        } else {
            System.setProperty(key, originalValue);
        }
    }
}
