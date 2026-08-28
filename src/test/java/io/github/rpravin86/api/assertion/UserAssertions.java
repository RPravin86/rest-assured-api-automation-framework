package io.github.rpravin86.api.assertion;

import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
import io.github.rpravin86.api.model.response.ErrorResponse;
import io.github.rpravin86.api.model.response.UserResponse;
import io.qameta.allure.Step;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reusable business-level assertions for GoREST user responses.
 */
public final class UserAssertions {

    private UserAssertions() {
        throw new IllegalStateException("Assertion class cannot be instantiated");
    }

    @Step("Validate created GoREST user")
    public static void assertCreatedUserMatchesRequest(
            UserResponse actual,
            CreateUserRequest expected) {

        assertThat(actual).as("created user response").isNotNull();
        assertThat(expected).as("create user request").isNotNull();
        assertThat(actual.getId()).as("created user ID").isPositive();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
    }

    @Step("Validate updated GoREST user")
    public static void assertUpdatedUserMatchesRequest(
            UserResponse actual,
            UpdateUserRequest expected) {

        assertThat(actual).as("updated user response").isNotNull();
        assertThat(expected).as("update user request").isNotNull();
        assertThat(actual.getId()).as("updated user ID").isPositive();

        if (expected.getName() != null) {
            assertThat(actual.getName()).isEqualTo(expected.getName());
        }
        if (expected.getEmail() != null) {
            assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        }
        if (expected.getGender() != null) {
            assertThat(actual.getGender()).isEqualTo(expected.getGender());
        }
        if (expected.getStatus() != null) {
            assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        }
    }

    @Step("Validate GoREST validation error")
    public static void assertValidationError(
            List<ErrorResponse> errors,
            String expectedField,
            String expectedMessage) {

        assertThat(errors)
                .as("validation errors")
                .isNotNull()
                .isNotEmpty()
                .anySatisfy(error -> {
                    assertThat(error.getField()).isEqualTo(expectedField);
                    assertThat(error.getMessage()).isEqualTo(expectedMessage);
                });
    }

    @Step("Validate GoREST error message")
    public static void assertErrorMessage(
            ErrorResponse error,
            String expectedMessage) {

        assertThat(error).as("error response").isNotNull();
        assertThat(error.getMessage()).isEqualTo(expectedMessage);
    }
}
