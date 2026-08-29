package io.github.rpravin86.api.tests.user;

import io.github.rpravin86.api.assertion.UserAssertions;
import io.github.rpravin86.api.base.BaseTest;
import io.github.rpravin86.api.builder.UserDataBuilder;
import io.github.rpravin86.api.client.UserClient;
import io.github.rpravin86.api.constants.FrameworkConstants;
import io.github.rpravin86.api.dataprovider.UserDataProvider;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.response.ErrorResponse;
import io.github.rpravin86.api.model.response.UserResponse;
import io.github.rpravin86.api.specification.ResponseSpecFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("GoREST API")
@Feature("User Error Handling")
public class UserNegativeTest extends BaseTest {

    @Test(groups = {"negative", "regression"})
    @Story("Create User Without Authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldRejectCreateUserWithoutAuthentication() {
        CreateUserRequest request = UserDataBuilder.validUser().build();

        Response response = userClient.createUserWithoutAuthentication(request);

        response.then()
                .spec(ResponseSpecFactory.unauthorized())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));

        ErrorResponse error = response.as(ErrorResponse.class);
        UserAssertions.assertErrorMessage(error, "Authentication failed");
    }

    @Test(groups = {"negative", "regression"})
    @Story("Create User With Duplicate Email")
    @Severity(SeverityLevel.NORMAL)
    public void shouldRejectDuplicateUserEmail() {
        CreateUserRequest request = UserDataBuilder.validUser().build();

        Response createResponse = userClient.createUser(request);
        createResponse.then().spec(ResponseSpecFactory.created());

        UserResponse createdUser = createResponse.as(UserResponse.class);
        registerUserForCleanup(createdUser.getId());

        Response duplicateResponse = userClient.createUser(request);

        duplicateResponse.then()
                .spec(ResponseSpecFactory.unprocessableEntity())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));

        List<ErrorResponse> errors = deserializeErrors(duplicateResponse);
        UserAssertions.assertValidationError(
                errors,
                "email",
                "has already been taken");
    }

    @Test(
            groups = {"negative", "regression"},
            dataProvider = "invalidUserRequests",
            dataProviderClass = UserDataProvider.class)
    @Story("Validate User Payload")
    @Severity(SeverityLevel.NORMAL)
    public void shouldRejectInvalidUserPayload(
            CreateUserRequest request,
            String expectedField,
            String expectedMessage) {

        Response response = userClient.createUser(request);

        response.then()
                .spec(ResponseSpecFactory.unprocessableEntity())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));

        List<ErrorResponse> errors = deserializeErrors(response);
        UserAssertions.assertValidationError(
                errors,
                expectedField,
                expectedMessage);
    }

    @Test(groups = {"negative", "regression"})
    @Story("Get Nonexistent User")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnNotFoundForNonexistentUser() {
        Response response = userClient.getAuthenticatedUser(Long.MAX_VALUE);

        response.then()
                .spec(ResponseSpecFactory.notFound())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));

        ErrorResponse error = response.as(ErrorResponse.class);
        UserAssertions.assertErrorMessage(error, "Resource not found");
    }

    private List<ErrorResponse> deserializeErrors(Response response) {
        return response.as(new TypeRef<>() {
        });
    }
}
