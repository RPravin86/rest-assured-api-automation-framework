package io.github.rpravin86.api.client;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserClientTest {

    private final UserClient userClient = new UserClient();

    @DataProvider(name = "invalidPagination")
    public Object[][] invalidPagination() {
        return new Object[][]{
                {0, 20},
                {-1, 20},
                {1, 0}
        };
    }

    @Test(dataProvider = "invalidPagination",
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp =
                    "Page and per-page values must be greater than zero")
    public void shouldRejectInvalidPagination(int page, int perPage) {
        userClient.getPublicUsers(page, perPage);
    }

    @Test(expectedExceptions = NullPointerException.class,
            expectedExceptionsMessageRegExp =
                    "Query parameters must not be null")
    public void shouldRejectNullQueryParameters() {
        userClient.getPublicUsers(null);
    }
}
