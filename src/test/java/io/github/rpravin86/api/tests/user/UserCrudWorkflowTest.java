package io.github.rpravin86.api.tests.user;

import io.github.rpravin86.api.assertion.UserAssertions;
import io.github.rpravin86.api.base.BaseTest;
import io.github.rpravin86.api.builder.UserDataBuilder;
import io.github.rpravin86.api.constants.FrameworkConstants;
import io.github.rpravin86.api.model.UserStatus;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
import io.github.rpravin86.api.model.response.ErrorResponse;
import io.github.rpravin86.api.model.response.UserResponse;
import io.github.rpravin86.api.specification.ResponseSpecFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("GoREST API")
@Feature("User Management")
public class UserCrudWorkflowTest extends BaseTest {

    @Test(groups = {"e2e", "regression"})
    @Story("Complete User CRUD Workflow")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldCompleteUserCrudLifecycleSuccessfully() {
        CreateUserRequest createRequest = UserDataBuilder.validUser().build();

        Response createResponse = userClient.createUser(createRequest);
        createResponse.then()
                .spec(ResponseSpecFactory.created())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USER_SCHEMA));

        UserResponse createdUser = createResponse.as(UserResponse.class);
        registerUserForCleanup(createdUser.getId());
        UserAssertions.assertCreatedUserMatchesRequest(
                createdUser,
                createRequest);

        Response getResponse = userClient.getAuthenticatedUser(
                createdUser.getId());
        getResponse.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USER_SCHEMA));

        UserResponse retrievedUser = getResponse.as(UserResponse.class);
        UserAssertions.assertRetrievedUserMatchesCreatedUser(
                retrievedUser,
                createdUser);

        UpdateUserRequest replaceRequest = UserDataBuilder
                .validFullUpdate()
                .build();

        Response replaceResponse = userClient.updateUser(
                retrievedUser.getId(),
                replaceRequest);
        replaceResponse.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USER_SCHEMA));

        UserResponse replacedUser = replaceResponse.as(UserResponse.class);
        UserAssertions.assertUpdatedUserMatchesRequest(
                replacedUser,
                retrievedUser,
                replaceRequest);

        UpdateUserRequest patchRequest = UserDataBuilder
                .statusOnlyUpdate(UserStatus.INACTIVE)
                .build();

        Response patchResponse = userClient.partiallyUpdateUser(
                replacedUser.getId(),
                patchRequest);
        patchResponse.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USER_SCHEMA));

        UserResponse patchedUser = patchResponse.as(UserResponse.class);
        UserAssertions.assertUpdatedUserMatchesRequest(
                patchedUser,
                replacedUser,
                patchRequest);

        Response deleteResponse = userClient.deleteUser(patchedUser.getId());
        deleteResponse.then()
                .spec(ResponseSpecFactory.noContent());

        unregisterUserFromCleanup(patchedUser.getId());

        Response deletedUserResponse = userClient.getAuthenticatedUser(
                patchedUser.getId());
        deletedUserResponse.then()
                .spec(ResponseSpecFactory.notFound())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.ERROR_SCHEMA));

        ErrorResponse error = deletedUserResponse.as(ErrorResponse.class);
        UserAssertions.assertErrorMessage(error, "Resource not found");
    }
}
