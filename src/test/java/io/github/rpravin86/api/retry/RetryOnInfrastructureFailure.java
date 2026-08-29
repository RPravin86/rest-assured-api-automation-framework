package io.github.rpravin86.api.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Opts a safe, idempotent test into infrastructure-only retry handling.
 *
 * <p>Do not use this annotation on tests that create or mutate remote data.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryOnInfrastructureFailure {
}
