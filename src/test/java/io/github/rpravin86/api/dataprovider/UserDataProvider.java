package io.github.rpravin86.api.dataprovider;

import io.github.rpravin86.api.builder.UserDataBuilder;
import org.testng.annotations.DataProvider;

/**
 * Supplies invalid GoREST user payloads and their expected validation errors.
 */
public final class UserDataProvider {

    private UserDataProvider() {
        throw new IllegalStateException("Data-provider class cannot be instantiated");
    }

    @DataProvider(name = "invalidUserRequests")
    public static Object[][] invalidUserRequests() {
        return new Object[][]{
                {
                        UserDataBuilder.validUser().name("").build(),
                        "name",
                        "can't be blank"
                },
                {
                        UserDataBuilder.validUser().email("").build(),
                        "email",
                        "can't be blank"
                },
                {
                        UserDataBuilder.validUser().email("invalid-email").build(),
                        "email",
                        "is invalid"
                },
                {
                        UserDataBuilder.validUser().gender(null).build(),
                        "gender",
                        "can't be blank, can be male of female"
                },
                {
                        UserDataBuilder.validUser().status(null).build(),
                        "status",
                        "can't be blank"
                }
        };
    }
}
