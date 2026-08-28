package io.github.rpravin86.api.model.request;

import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body used to create a GoREST user.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String name;
    private String email;
    private Gender gender;
    private UserStatus status;
}
