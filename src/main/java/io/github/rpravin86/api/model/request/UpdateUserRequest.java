package io.github.rpravin86.api.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body used for both full and partial GoREST user updates.
 * Unset values are omitted so the same model can represent a PATCH payload.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {

    private String name;
    private String email;
    private Gender gender;
    private UserStatus status;
}
