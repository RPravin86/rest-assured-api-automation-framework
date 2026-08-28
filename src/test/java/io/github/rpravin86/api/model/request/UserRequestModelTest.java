package io.github.rpravin86.api.model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rpravin86.api.model.Gender;
import io.github.rpravin86.api.model.UserStatus;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRequestModelTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldSerializeCreateUserRequestWithApiValues()
            throws JsonProcessingException {

        CreateUserRequest request = CreateUserRequest.builder()
                .name("API User")
                .email("api.user@example.com")
                .gender(Gender.FEMALE)
                .status(UserStatus.ACTIVE)
                .build();

        JsonNode requestJson = objectMapper.readTree(
                objectMapper.writeValueAsString(request));

        assertThat(requestJson.path("name").asText())
                .isEqualTo("API User");
        assertThat(requestJson.path("email").asText())
                .isEqualTo("api.user@example.com");
        assertThat(requestJson.path("gender").asText())
                .isEqualTo("female");
        assertThat(requestJson.path("status").asText())
                .isEqualTo("active");
    }

    @Test
    public void shouldOmitUnsetFieldsFromPartialUpdate()
            throws JsonProcessingException {

        UpdateUserRequest request = UpdateUserRequest.builder()
                .status(UserStatus.INACTIVE)
                .build();

        JsonNode requestJson = objectMapper.readTree(
                objectMapper.writeValueAsString(request));

        assertThat(requestJson.path("status").asText())
                .isEqualTo("inactive");
        assertThat(requestJson.has("name")).isFalse();
        assertThat(requestJson.has("email")).isFalse();
        assertThat(requestJson.has("gender")).isFalse();
    }
}
