package io.github.rpravin86.api.tests.user;

import io.github.rpravin86.api.client.UserClient;
import io.github.rpravin86.api.constants.FrameworkConstants;
import io.github.rpravin86.api.model.UserStatus;
import io.github.rpravin86.api.model.response.UserResponse;
import io.github.rpravin86.api.retry.RetryOnInfrastructureFailure;
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
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("GoREST API")
@Feature("Public User Collection")
public class GetPublicUsersTest {

    private static final String PAGINATION_TOTAL = "X-Pagination-Total";
    private static final String PAGINATION_PAGES = "X-Pagination-Pages";
    private static final String PAGINATION_PAGE = "X-Pagination-Page";
    private static final String PAGINATION_LIMIT = "X-Pagination-Limit";

    private final UserClient userClient = new UserClient();

    @Test(groups = {"smoke", "regression"})
    @RetryOnInfrastructureFailure
    @Story("List Public Users")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldGetPublicUsersSuccessfully() {
        Response response = userClient.getPublicUsers();

        response.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USERS_SCHEMA));

        List<UserResponse> users = deserializeUsers(response);
        assertThat(users).as("public users").isNotEmpty();
    }

    @Test(groups = "regression")
    @RetryOnInfrastructureFailure
    @Story("Paginate Public Users")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnRequestedPublicUsersPage() {
        int requestedPage = 2;
        int requestedPageSize = 5;

        Response response = userClient.getPublicUsers(
                requestedPage,
                requestedPageSize);

        response.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USERS_SCHEMA));

        List<UserResponse> users = deserializeUsers(response);

        assertThat(users)
                .as("paginated public users")
                .isNotEmpty()
                .hasSizeLessThanOrEqualTo(requestedPageSize);
        assertThat(response.getHeader(PAGINATION_PAGE))
                .isEqualTo(String.valueOf(requestedPage));
        assertThat(response.getHeader(PAGINATION_LIMIT))
                .isEqualTo(String.valueOf(requestedPageSize));
        assertThat(Integer.parseInt(response.getHeader(PAGINATION_TOTAL)))
                .isPositive();
        assertThat(Integer.parseInt(response.getHeader(PAGINATION_PAGES)))
                .isPositive();
    }

    @Test(groups = "regression")
    @RetryOnInfrastructureFailure
    @Story("Filter Public Users")
    @Severity(SeverityLevel.NORMAL)
    public void shouldFilterPublicUsersByStatus() {
        UserStatus expectedStatus = UserStatus.ACTIVE;

        Response response = userClient.getPublicUsers(Map.of(
                "status", expectedStatus.getApiValue(),
                "per_page", 20));

        response.then()
                .spec(ResponseSpecFactory.ok())
                .body(matchesJsonSchemaInClasspath(
                        FrameworkConstants.USERS_SCHEMA));

        List<UserResponse> users = deserializeUsers(response);

        assertThat(users)
                .as("filtered public users")
                .isNotEmpty()
                .allSatisfy(user -> assertThat(user.getStatus())
                        .isEqualTo(expectedStatus));
    }

    private List<UserResponse> deserializeUsers(Response response) {
        return response.as(new TypeRef<>() {
        });
    }
}
