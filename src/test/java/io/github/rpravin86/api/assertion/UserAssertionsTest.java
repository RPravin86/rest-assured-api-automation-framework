package io.github.rpravin86.api.assertion;

import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
import io.github.rpravin86.api.model.response.ErrorResponse;
import io.github.rpravin86.api.model.response.UserResponse;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserAssertionsTest {

    @Test
    public void shouldAcceptMatchingCreatedUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("API User")
                .email("api.user@example.com")
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE)
                .build();

        UserResponse response = new UserResponse(
                101L,
                "API User",
                "api.user@example.com",
                Gender.MALE,
                UserStatus.ACTIVE);

        assertThatCode(() ->
                UserAssertions.assertCreatedUserMatchesRequest(
                        response, request))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldRejectMismatchedCreatedUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Expected User")
                .email("api.user@example.com")
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE)
                .build();

        UserResponse response = new UserResponse(
                101L,
                "Different User",
                "api.user@example.com",
                Gender.MALE,
                UserStatus.ACTIVE);

        assertThatThrownBy(() ->
                UserAssertions.assertCreatedUserMatchesRequest(
                        response, request))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    public void shouldAcceptRetrievedUserMatchingCreatedUser() {
        UserResponse createdUser = new UserResponse(
                101L,
                "API User",
                "api.user@example.com",
                Gender.MALE,
                UserStatus.ACTIVE);

        UserResponse retrievedUser = new UserResponse(
                101L,
                "API User",
                "api.user@example.com",
                Gender.MALE,
                UserStatus.ACTIVE);

        assertThatCode(() ->
                UserAssertions.assertRetrievedUserMatchesCreatedUser(
                        retrievedUser, createdUser))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldValidateOnlyFieldsPresentInPartialUpdate() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .status(UserStatus.INACTIVE)
                .build();

        UserResponse response = new UserResponse(
                101L,
                "Existing User",
                "existing.user@example.com",
                Gender.FEMALE,
                UserStatus.INACTIVE);

        assertThatCode(() ->
                UserAssertions.assertUpdatedUserMatchesRequest(
                        response, request))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldFindExpectedValidationError() {
        List<ErrorResponse> errors = List.of(
                new ErrorResponse(
                        "email",
                        "has already been taken"));

        assertThatCode(() -> UserAssertions.assertValidationError(
                errors,
                "email",
                "has already been taken"))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldValidateMessageOnlyError() {
        ErrorResponse error = new ErrorResponse(
                null,
                "Authentication failed");

        assertThatCode(() -> UserAssertions.assertErrorMessage(
                error,
                "Authentication failed"))
                .doesNotThrowAnyException();
    }
}
