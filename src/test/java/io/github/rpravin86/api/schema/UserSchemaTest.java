package io.github.rpravin86.api.schema;

import io.github.rpravin86.api.constants.FrameworkConstants;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserSchemaTest {

    @Test
    public void shouldValidateSingleUserSchema() {
        String responseBody = """
                {
                  "id": 101,
                  "name": "API User",
                  "email": "api.user@example.com",
                  "gender": "male",
                  "status": "active"
                }
                """;

        assertThat(
                responseBody,
                matchesJsonSchemaInClasspath(
                        FrameworkConstants.USER_SCHEMA));
    }

    @Test
    public void shouldValidateUserCollectionSchema() {
        String responseBody = """
                [
                  {
                    "id": 101,
                    "name": "API User",
                    "email": "api.user@example.com",
                    "gender": "female",
                    "status": "inactive"
                  }
                ]
                """;

        assertThat(
                responseBody,
                matchesJsonSchemaInClasspath(
                        FrameworkConstants.USERS_SCHEMA));
    }

    @Test
    public void shouldValidateMessageErrorSchema() {
        String responseBody = """
                {
                  "message": "Authentication failed"
                }
                """;

        assertThat(
                responseBody,
                matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));
    }

    @Test
    public void shouldValidateValidationErrorSchema() {
        String responseBody = """
                [
                  {
                    "field": "email",
                    "message": "has already been taken"
                  }
                ]
                """;

        assertThat(
                responseBody,
                matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));
    }
}
