package io.github.rpravin86.api.retry;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class InfrastructureFailureClassifierTest {

    @DataProvider(name = "retryableFailures")
    public Object[][] retryableFailures() {
        return new Object[][]{
                {new SocketTimeoutException("Read timed out")},
                {new RuntimeException("wrapper", new ConnectException("Connection refused"))},
                {new AssertionError("Expected status code <200> but was <503>.")},
                {new AssertionError("HTTP 429 returned by upstream service")}
        };
    }

    @Test(dataProvider = "retryableFailures")
    public void shouldRecognizeInfrastructureFailures(Throwable failure) {
        assertThat(InfrastructureFailureClassifier.isRetryable(failure)).isTrue();
    }

    @Test
    public void shouldNotRetryProductOrAuthenticationFailures() {
        assertThat(InfrastructureFailureClassifier.isRetryable(
                new AssertionError("Expected status code <200> but was <401>.")))
                .isFalse();
        assertThat(InfrastructureFailureClassifier.isRetryable(
                new AssertionError("Expected status code <503> but was <401>.")))
                .isFalse();
        assertThat(InfrastructureFailureClassifier.isRetryable(
                new AssertionError("Expected user name to match")))
                .isFalse();
    }
}
