package io.github.rpravin86.api.client;

import io.github.rpravin86.api.constants.ApiRoutes;
import io.github.rpravin86.api.model.request.CreateUserRequest;
import io.github.rpravin86.api.model.request.UpdateUserRequest;
import io.github.rpravin86.api.specification.RequestSpecFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

/**
 * Executes GoREST user-resource operations without performing test assertions.
 */
public final class UserClient {

    @Step("Create a GoREST user")
    public Response createUser(CreateUserRequest request) {
        return authenticatedRequestWithBody(request)
                .post(ApiRoutes.USERS);
    }

    @Step("Get authenticated GoREST user with ID {userId}")
    public Response getAuthenticatedUser(long userId) {
        return authenticatedRequest()
                .when()
                .get(ApiRoutes.userById(userId));
    }

    @Step("Get public GoREST user with ID {userId}")
    public Response getPublicUser(long userId) {
        return publicRequest()
                .when()
                .get(ApiRoutes.userById(userId));
    }

    @Step("Get all public GoREST users")
    public Response getPublicUsers() {
        return publicRequest()
                .when()
                .get(ApiRoutes.USERS);
    }

    @Step("Get filtered public GoREST users")
    public Response getPublicUsers(Map<String, ?> queryParameters) {
        Objects.requireNonNull(
                queryParameters,
                "Query parameters must not be null");

        return publicRequest()
                .queryParams(queryParameters)
                .when()
                .get(ApiRoutes.USERS);
    }

    @Step("Get public GoREST users from page {page} with {perPage} records")
    public Response getPublicUsers(int page, int perPage) {
        validatePagination(page, perPage);

        return getPublicUsers(Map.of(
                "page", page,
                "per_page", perPage));
    }

    @Step("Update GoREST user with ID {userId}")
    public Response updateUser(
            long userId,
            UpdateUserRequest request) {

        return authenticatedRequestWithBody(request)
                .put(ApiRoutes.userById(userId));
    }

    @Step("Partially update GoREST user with ID {userId}")
    public Response partiallyUpdateUser(
            long userId,
            UpdateUserRequest request) {

        return authenticatedRequestWithBody(request)
                .patch(ApiRoutes.userById(userId));
    }

    @Step("Delete GoREST user with ID {userId}")
    public Response deleteUser(long userId) {
        return authenticatedRequest()
                .when()
                .delete(ApiRoutes.userById(userId));
    }

    private RequestSpecification authenticatedRequestWithBody(Object body) {
        Objects.requireNonNull(body, "Request body must not be null");

        return authenticatedRequest()
                .body(body)
                .when();
    }

    private RequestSpecification authenticatedRequest() {
        return given()
                .spec(RequestSpecFactory.authenticatedRequestSpec());
    }

    private RequestSpecification publicRequest() {
        return given()
                .spec(RequestSpecFactory.publicRequestSpec());
    }

    private void validatePagination(int page, int perPage) {
        if (page <= 0 || perPage <= 0) {
            throw new IllegalArgumentException(
                    "Page and per-page values must be greater than zero");
        }
    }
}
