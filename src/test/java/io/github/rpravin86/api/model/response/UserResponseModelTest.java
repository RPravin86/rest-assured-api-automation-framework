package io.github.rpravin86.api.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseModelTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldDeserializeUserResponse()
            throws JsonProcessingException {

        String responseBody = """
                {
                  "id": 101,
                  "name": "API User",
                  "email": "api.user@example.com",
                  "gender": "male",
                  "status": "active"
                }
                """;

        UserResponse response = objectMapper.readValue(
                responseBody, UserResponse.class);

        assertThat(response.getId()).isEqualTo(101L);
        assertThat(response.getName()).isEqualTo("API User");
        assertThat(response.getEmail()).isEqualTo("api.user@example.com");
        assertThat(response.getGender()).isEqualTo(Gender.MALE);
        assertThat(response.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void shouldDeserializeValidationErrorList()
            throws JsonProcessingException {

        String responseBody = """
                [
                  {
                    "field": "email",
                    "message": "has already been taken"
                  }
                ]
                """;

        List<ErrorResponse> errors = objectMapper.readValue(
                responseBody,
                new TypeReference<>() {
                });

        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).getField()).isEqualTo("email");
        assertThat(errors.get(0).getMessage())
                .isEqualTo("has already been taken");
    }

    @Test
    public void shouldDeserializeMessageOnlyError()
            throws JsonProcessingException {

        String responseBody = """
                {
                  "message": "Authentication failed"
                }
                """;

        ErrorResponse error = objectMapper.readValue(
                responseBody, ErrorResponse.class);

        assertThat(error.getField()).isNull();
        assertThat(error.getMessage()).isEqualTo("Authentication failed");
    }
}
