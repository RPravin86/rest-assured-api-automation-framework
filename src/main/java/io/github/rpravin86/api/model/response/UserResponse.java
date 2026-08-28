package io.github.rpravin86.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Successful GoREST user response.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private long id;
    private String name;
    private String email;
    private Gender gender;
    private UserStatus status;
}
