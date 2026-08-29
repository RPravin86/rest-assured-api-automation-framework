package io.github.rpravin86.api.listener;

import org.testng.annotations.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class TestLifecycleListenerTest {

    @Test
    public void shouldBuildSafeAllureEnvironmentMetadata() {
        Properties metadata =
                TestLifecycleListener.buildEnvironmentProperties();

        assertThat(metadata)
                .containsKeys(
                        "Environment",
                        "Base URL",
                        "Base path",
                        "Java version",
                        "OS")
                .doesNotContainKeys(
                        "Authorization",
                        "GOREST_API_TOKEN",
                        "gorest.api.token");
    }
}
