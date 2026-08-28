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

    @Step("Get GoREST user with ID {userId}")
    public Response getUser(long userId) {
        return publicRequest()
                .get(ApiRoutes.userById(userId));
    }

    @Step("Get all GoREST users")
    public Response getUsers() {
        return publicRequest()
                .get(ApiRoutes.USERS);
    }

    @Step("Get filtered GoREST users")
    public Response getUsers(Map<String, ?> queryParameters) {
        Objects.requireNonNull(
                queryParameters,
                "Query parameters must not be null");

        return publicRequest()
                .queryParams(queryParameters)
                .get(ApiRoutes.USERS);
    }

    @Step("Get GoREST users from page {page} with {perPage} records")
    public Response getUsers(int page, int perPage) {
        validatePagination(page, perPage);

        return getUsers(Map.of(
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
        return given()
                .spec(RequestSpecFactory.authenticatedRequestSpec())
                .when()
                .delete(ApiRoutes.userById(userId));
    }

    private RequestSpecification authenticatedRequestWithBody(Object body) {
        Objects.requireNonNull(body, "Request body must not be null");

        return given()
                .spec(RequestSpecFactory.authenticatedRequestSpec())
                .body(body)
                .when();
    }

    private RequestSpecification publicRequest() {
        return given()
                .spec(RequestSpecFactory.publicRequestSpec())
                .when();
    }

    private void validatePagination(int page, int perPage) {
        if (page <= 0 || perPage <= 0) {
            throw new IllegalArgumentException(
                    "Page and per-page values must be greater than zero");
        }
    }
}
