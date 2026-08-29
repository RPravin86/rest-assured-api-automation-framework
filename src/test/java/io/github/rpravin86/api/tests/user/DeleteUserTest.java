package io.github.rpravin86.api.tests.user;

import io.github.rpravin86.api.assertion.UserAssertions;
import io.github.rpravin86.api.base.BaseTest;
import io.github.rpravin86.api.builder.UserDataBuilder;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.response.ErrorResponse;
import io.github.rpravin86.api.model.response.UserResponse;
import io.github.rpravin86.api.specification.ResponseSpecFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("GoREST API")
@Feature("User Management")
public class DeleteUserTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Delete User")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldDeleteExistingUserSuccessfully() {
        UserResponse createdUser = createUserForDeleteTest();
        long userId = createdUser.getId();

        Response deleteResponse = userClient.deleteUser(userId);

        deleteResponse.then()
                .spec(ResponseSpecFactory.noContent());

        unregisterUserFromCleanup(userId);

        Response getResponse = userClient.getAuthenticatedUser(userId);

        getResponse.then()
                .spec(ResponseSpecFactory.notFound())
                .body(matchesJsonSchemaInClasspath("schemas/error-schema.json"));

        ErrorResponse error = getResponse.as(ErrorResponse.class);
        UserAssertions.assertErrorMessage(error, "Resource not found");
    }

    @Step("Create a user for delete testing")
    private UserResponse createUserForDeleteTest() {
        CreateUserRequest request = UserDataBuilder.validUser().build();
        Response response = userClient.createUser(request);

        response.then().spec(ResponseSpecFactory.created());

        UserResponse createdUser = response.as(UserResponse.class);
        registerUserForCleanup(createdUser.getId());
        return createdUser;
    }
}
