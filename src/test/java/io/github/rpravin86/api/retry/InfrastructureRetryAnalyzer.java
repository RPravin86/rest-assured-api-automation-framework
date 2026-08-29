package io.github.rpravin86.api.retry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/** Retries an opted-in test only when its failure is infrastructure-related. */
public final class InfrastructureRetryAnalyzer implements IRetryAnalyzer {

    static final String MAX_ATTEMPTS_PROPERTY = "retry.max.attempts";
    private static final int DEFAULT_MAX_ATTEMPTS = 1;
    private static final Logger LOGGER =
            LogManager.getLogger(InfrastructureRetryAnalyzer.class);

    private final AtomicInteger retryCount = new AtomicInteger();

    @Override
    public boolean retry(ITestResult result) {
        Throwable failure = result.getThrowable();
        int maximumRetries = Math.max(
                0,
                Integer.getInteger(MAX_ATTEMPTS_PROPERTY, DEFAULT_MAX_ATTEMPTS));

        if (!InfrastructureFailureClassifier.isRetryable(failure)) {
            return false;
        }

        int attempt = retryCount.incrementAndGet();
        if (attempt > maximumRetries) {
            return false;
        }

        LOGGER.warn(
                "Retrying {} after infrastructure failure ({}/{}): {}",
                result.getMethod().getQualifiedName(),
                attempt,
                maximumRetries,
                failure == null ? "unknown failure" : failure.toString());
        return true;
    }
}
