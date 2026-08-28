package io.github.rpravin86.api.builder;

import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
import io.github.rpravin86.api.util.RandomDataGenerator;

import java.util.Objects;

/**
 * Provides valid, customizable request builders for GoREST user tests.
 */
public final class UserDataBuilder {

    private UserDataBuilder() {
        throw new IllegalStateException("Builder factory cannot be instantiated");
    }

    public static CreateUserRequest.CreateUserRequestBuilder validUser() {
        return CreateUserRequest.builder()
                .name(RandomDataGenerator.uniqueName())
                .email(RandomDataGenerator.uniqueEmail())
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE);
    }

    public static UpdateUserRequest.UpdateUserRequestBuilder validFullUpdate() {
        return UpdateUserRequest.builder()
                .name(RandomDataGenerator.uniqueName())
                .email(RandomDataGenerator.uniqueEmail())
                .gender(Gender.FEMALE)
                .status(UserStatus.ACTIVE);
    }

    public static UpdateUserRequest.UpdateUserRequestBuilder statusOnlyUpdate(
            UserStatus status) {

        return UpdateUserRequest.builder()
                .status(Objects.requireNonNull(
                        status,
                        "User status must not be null"));
    }
}
