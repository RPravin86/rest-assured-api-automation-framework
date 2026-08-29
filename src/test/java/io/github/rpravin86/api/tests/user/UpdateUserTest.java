package io.github.rpravin86.api.tests.user;

import io.github.rpravin86.api.assertion.UserAssertions;
import io.github.rpravin86.api.base.BaseTest;
import io.github.rpravin86.api.builder.UserDataBuilder;
import io.github.rpravin86.api.model.UserStatus;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
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
public class UpdateUserTest extends BaseTest {

    @Test(groups = "regression")
    @Story("Replace User")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReplaceExistingUserSuccessfully() {
        UserResponse createdUser = createUserForUpdateTest();
        UpdateUserRequest request = UserDataBuilder.validFullUpdate().build();

        Response response = userClient.updateUser(
                createdUser.getId(),
                request);

        response.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));

        UserResponse updatedUser = response.as(UserResponse.class);

        UserAssertions.assertUpdatedUserMatchesRequest(
                updatedUser,
                createdUser,
                request);
    }

    @Test(groups = "regression")
    @Story("Partially Update User")
    @Severity(SeverityLevel.NORMAL)
    public void shouldPartiallyUpdateExistingUserSuccessfully() {
        UserResponse createdUser = createUserForUpdateTest();
        UpdateUserRequest request = UserDataBuilder
                .statusOnlyUpdate(UserStatus.INACTIVE)
                .build();

        Response response = userClient.partiallyUpdateUser(
                createdUser.getId(),
                request);

        response.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));

        UserResponse updatedUser = response.as(UserResponse.class);

        UserAssertions.assertUpdatedUserMatchesRequest(
                updatedUser,
                createdUser,
                request);
    }

    @Step("Create a user for update testing")
    private UserResponse createUserForUpdateTest() {
        CreateUserRequest request = UserDataBuilder.validUser().build();
        Response response = userClient.createUser(request);

        response.then().spec(ResponseSpecFactory.created());

        UserResponse createdUser = response.as(UserResponse.class);
        registerUserForCleanup(createdUser.getId());
        return createdUser;
    }
}
