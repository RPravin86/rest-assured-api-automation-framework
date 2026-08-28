# REST Assured API Automation Framework

A scalable API test automation framework built with Java 17, REST Assured, TestNG, Jackson, Lombok, Log4j2 and Allure Report.

## Current status

The Maven foundation, configuration layer, API routes, reusable specifications and user request models are in place. API clients, response models and endpoint tests will be added incrementally.

The configuration layer supports `dev` and `qa` environments, JVM property overrides and secure runtime token resolution.

Request specifications centralize the base URI, base path, JSON headers, authentication and HTTP timeouts. Response specifications provide reusable HTTP contract checks for the status codes used by the GoREST user workflow.

All API calls include request and response attachments in Allure. Authentication and API-key headers are redacted in reports and REST Assured logs without modifying the real request sent to GoREST.

Typed user request models serialize GoREST gender and status values safely. `UpdateUserRequest` supports both complete PUT payloads and partial PATCH payloads by omitting fields that were not set.

## Prerequisites

- Java 17
- Maven 3.9+
- Allure command-line tool for viewing reports
- A GoREST bearer token for write operations

## Token setup

Set the token as an environment variable. Never add the real value to project files.

```bash
export GOREST_API_TOKEN="your-token"
```

PowerShell:

```powershell
$Env:GOREST_API_TOKEN = "your-token"
```

## Commands

Compile and run the configured TestNG suite:

```bash
mvn clean test
```

Select an environment:

```bash
mvn clean test -Dtest.environment=dev
```

Environment selection follows this order:

1. `-Dtest.environment=<name>` JVM system property
2. `TEST_ENVIRONMENT` operating-system environment variable
3. `qa` default

For authenticated requests, the token is resolved from `-Dgorest.api.token=<token>` first and `GOREST_API_TOKEN` second.

Open an Allure report after tests produce results:

```bash
allure serve target/allure-results
```

## Planned coverage

- User CRUD API client
- Response models
- Positive, negative and schema-validation tests
- Secure request/response reporting
- Automatic test-data cleanup
- GitHub Actions execution
