package io.github.rpravin86.api.constants;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiRoutesTest {

    @Test
    public void shouldBuildUserRouteFromValidId() {
        assertThat(ApiRoutes.userById(123L))
                .isEqualTo("/users/123");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "User ID must be greater than zero")
    public void shouldRejectNonPositiveUserId() {
        ApiRoutes.userById(0L);
    }
}
