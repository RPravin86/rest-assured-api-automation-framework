package io.github.rpravin86.api.retry;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/** Connects the retry analyzer only to explicitly opted-in test methods. */
public final class RetryAnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(
            ITestAnnotation annotation,
            Class testClass,
            Constructor testConstructor,
            Method testMethod) {

        if (testMethod != null
                && testMethod.isAnnotationPresent(RetryOnInfrastructureFailure.class)) {
            annotation.setRetryAnalyzer(InfrastructureRetryAnalyzer.class);
        }
    }
}
