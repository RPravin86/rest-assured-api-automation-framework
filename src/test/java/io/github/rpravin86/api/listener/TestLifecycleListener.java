package io.github.rpravin86.api.listener;

import io.github.rpravin86.api.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/** Adds execution metadata to Allure and logs suite-level test results. */
public final class TestLifecycleListener
        implements IExecutionListener, ISuiteListener {

    private static final Logger LOGGER =
            LogManager.getLogger(TestLifecycleListener.class);
    private static final Path ALLURE_RESULTS =
            Path.of("target", "allure-results");
    private static final String CATEGORIES_RESOURCE = "categories.json";

    @Override
    public void onExecutionStart() {
        LOGGER.info(
                "Starting API automation against {} ({})",
                ConfigManager.getEnvironment().getName(),
                ConfigManager.getBaseUrl());
        writeAllureMetadata();
    }

    @Override
    public void onStart(ISuite suite) {
        LOGGER.info("Starting TestNG suite: {}", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (ISuiteResult suiteResult : suite.getResults().values()) {
            passed += suiteResult.getTestContext().getPassedTests().size();
            failed += suiteResult.getTestContext().getFailedTests().size();
            skipped += suiteResult.getTestContext().getSkippedTests().size();
        }

        LOGGER.info(
                "Finished TestNG suite: {} [passed={}, failed={}, skipped={}]",
                suite.getName(),
                passed,
                failed,
                skipped);
    }

    @Override
    public void onExecutionFinish() {
        LOGGER.info("API automation execution finished");
    }

    private void writeAllureMetadata() {
        try {
            Files.createDirectories(ALLURE_RESULTS);
            writeEnvironmentProperties();
            copyCategories();
        } catch (IOException exception) {
            LOGGER.warn("Unable to write Allure execution metadata", exception);
        }
    }

    private void writeEnvironmentProperties() throws IOException {
        Properties environment = buildEnvironmentProperties();
        Path destination = ALLURE_RESULTS.resolve("environment.properties");

        try (OutputStream outputStream = Files.newOutputStream(destination)) {
            environment.store(outputStream, "API automation execution environment");
        }
    }

    static Properties buildEnvironmentProperties() {
        Properties environment = new Properties();
        environment.setProperty(
                "Environment",
                ConfigManager.getEnvironment().getName());
        environment.setProperty("Base URL", ConfigManager.getBaseUrl());
        environment.setProperty("Base path", ConfigManager.getBasePath());
        environment.setProperty("Java version", System.getProperty("java.version"));
        environment.setProperty("OS", System.getProperty("os.name"));
        return environment;
    }

    private void copyCategories() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = TestLifecycleListener.class.getClassLoader();
        }

        try (InputStream inputStream =
                     classLoader.getResourceAsStream(CATEGORIES_RESOURCE)) {
            if (inputStream == null) {
                throw new IOException(
                        "Allure categories resource was not found: "
                                + CATEGORIES_RESOURCE);
            }

            Files.copy(
                    inputStream,
                    ALLURE_RESULTS.resolve(CATEGORIES_RESOURCE),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
