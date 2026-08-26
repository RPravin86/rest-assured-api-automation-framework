package io.github.rpravin86.api.config;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentTest {

    @DataProvider(name = "validEnvironments")
    public Object[][] validEnvironments() {
        return new Object[][]{
                {"dev", Environment.DEV},
                {"DEV", Environment.DEV},
                {" qa ", Environment.QA}
        };
    }

    @Test(dataProvider = "validEnvironments")
    public void shouldResolveSupportedEnvironment(
            String suppliedValue,
            Environment expectedEnvironment) {

        assertThat(Environment.from(suppliedValue))
                .isEqualTo(expectedEnvironment);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*Unsupported environment.*")
    public void shouldRejectUnsupportedEnvironment() {
        Environment.from("production");
    }
}
