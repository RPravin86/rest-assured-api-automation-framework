package io.github.rpravin86.api.base;

import io.github.rpravin86.api.client.UserClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Provides shared API clients and per-test cleanup for created GoREST users.
 */
public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    protected final UserClient userClient = new UserClient();

    private final ThreadLocal<Set<Long>> createdUserIds =
            ThreadLocal.withInitial(LinkedHashSet::new);

    /**
     * Registers a created user for deletion after the current test method.
     *
     * @param userId positive GoREST user ID
     */
    protected void registerUserForCleanup(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Cleanup user ID must be greater than zero");
        }

        createdUserIds.get().add(userId);
    }

    @AfterMethod(alwaysRun = true)
    @Step("Delete users created during the test")
    protected void cleanUpCreatedUsers() {
        try {
            createdUserIds.get().forEach(this::deleteUserSafely);
        } finally {
            createdUserIds.remove();
        }
    }

    private void deleteUserSafely(long userId) {
        try {
            Response response = userClient.deleteUser(userId);
            int statusCode = response.statusCode();

            if (statusCode != 204 && statusCode != 404) {
                LOGGER.warn(
                        "Cleanup returned status {} for GoREST user {}",
                        statusCode,
                        userId);
            }
        } catch (RuntimeException exception) {
            LOGGER.warn(
                    "Unable to clean up GoREST user {}: {}",
                    userId,
                    exception.getMessage());
        }
    }
}
