package io.github.rpravin86.api.specification;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

/**
 * Provides reusable HTTP-level response expectations.
 * Resource-specific business assertions remain in assertion classes.
 */
public final class ResponseSpecFactory {

    private ResponseSpecFactory() {
        throw new IllegalStateException("Factory class cannot be instantiated");
    }

    public static ResponseSpecification ok() {
        return jsonResponseWithStatus(200);
    }

    public static ResponseSpecification created() {
        return jsonResponseWithStatus(201);
    }

    public static ResponseSpecification noContent() {
        return new ResponseSpecBuilder()
                .expectStatusCode(204)
                .build();
    }

    public static ResponseSpecification unauthorized() {
        return jsonResponseWithStatus(401);
    }

    public static ResponseSpecification notFound() {
        return jsonResponseWithStatus(404);
    }

    public static ResponseSpecification unprocessableEntity() {
        return jsonResponseWithStatus(422);
    }

    private static ResponseSpecification jsonResponseWithStatus(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
