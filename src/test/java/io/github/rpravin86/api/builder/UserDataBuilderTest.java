package io.github.rpravin86.api.builder;

import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDataBuilderTest {

    @Test
    public void shouldBuildValidUserWithUniqueData() {
        CreateUserRequest request = UserDataBuilder.validUser().build();

        assertThat(request.getName()).startsWith("API User ");
        assertThat(request.getEmail())
                .startsWith("api.user.")
                .endsWith("@example.com");
        assertThat(request.getGender()).isEqualTo(Gender.MALE);
        assertThat(request.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void shouldGenerateDifferentDataForEachUser() {
        CreateUserRequest firstUser = UserDataBuilder.validUser().build();
        CreateUserRequest secondUser = UserDataBuilder.validUser().build();

        assertThat(firstUser.getName()).isNotEqualTo(secondUser.getName());
        assertThat(firstUser.getEmail()).isNotEqualTo(secondUser.getEmail());
    }

    @Test
    public void shouldBuildCompleteUpdateRequest() {
        UpdateUserRequest request = UserDataBuilder.validFullUpdate().build();

        assertThat(request.getName()).isNotBlank();
        assertThat(request.getEmail()).endsWith("@example.com");
        assertThat(request.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(request.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void shouldBuildStatusOnlyUpdateRequest() {
        UpdateUserRequest request = UserDataBuilder
                .statusOnlyUpdate(UserStatus.INACTIVE)
                .build();

        assertThat(request.getStatus()).isEqualTo(UserStatus.INACTIVE);
        assertThat(request.getName()).isNull();
        assertThat(request.getEmail()).isNull();
        assertThat(request.getGender()).isNull();
    }
}
