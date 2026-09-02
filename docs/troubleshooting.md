# Troubleshooting guide

## `GoREST API token is missing`

Set the token in the same terminal or process that starts Maven:

```bash
export GOREST_API_TOKEN="your-token"
mvn clean test
```

For IntelliJ TestNG execution, add `GOREST_API_TOKEN` under the run configuration's environment variables. A variable exported in a terminal is not automatically available to an IDE launched elsewhere.

## GitHub Actions reports that the secret is missing

Create an Actions repository secret under:

```text
Repository → Settings → Secrets and variables → Actions
```

The name must match exactly:

```text
GOREST_API_TOKEN
```

Use a secret, not a repository variable. Workflows triggered from forks do not receive repository secrets.

## `zsh: command not found: allure`

Install the Allure command-line tool:

```bash
brew install allure
```

Alternatively:

```bash
npm install --global allure-commandline@2.35.1
```

Then confirm installation:

```bash
allure --version
```

## Allure report is empty or missing

Run the tests before opening the report:

```bash
mvn clean test
allure serve target/allure-results
```

Confirm that `target/allure-results` contains JSON result files plus `environment.properties` and `categories.json`.

## Expected `200` but received `404`

GoREST data can change and shared resources can disappear. Tests that validate an authenticated user must create their own user first, use the returned ID, and register the ID for cleanup. Public pagination tests should use the public collection rather than assuming a fixed user ID exists.

## Expected `201` but received `401`

The token is missing, invalid or expired. Generate a current token from the GoREST account page and update the environment variable or CI secret. Do not add `Bearer` to the stored token; the request specification adds the prefix.

## Expected `201` but received `422`

Common causes are a duplicate email or an invalid enum value. The builder generates UUID-based emails for normal tests. When constructing payloads manually, use a unique email and valid GoREST values:

```text
gender: male | female
status: active | inactive
```

## Maven warns that `test.environment` was overwritten

Surefire may report that the configured property was overwritten by a Maven user property when `-Dtest.environment` is supplied. This is informational: the explicit command-line environment takes precedence as intended.

## TestNG runs no tests

Run Maven from the repository root and confirm the selected suite exists:

```bash
mvn clean test -Dtest.suite=src/test/resources/testng.xml
```

For parallel integration execution:

```bash
mvn clean test \
  -Dtest.suite=src/test/resources/testng-parallel.xml
```

## Parallel execution leaves test data behind

Inspect `target/logs/api-automation.log` for cleanup warnings. Confirm that every successful creation immediately calls `registerUserForCleanup`. Do not share static user IDs between tests.

## A test was retried unexpectedly

Retries require the `@RetryOnInfrastructureFailure` annotation and a classified transient failure. Set the maximum to zero to confirm behavior without retries:

```bash
mvn clean test -Dretry.max.attempts=0
```

Mutating tests must never use the retry annotation.

## Jenkins cannot find Java, Maven or the token

Match the tool and credential names expected by the `Jenkinsfile`:

```text
JDK tool: jdk17
Maven tool: maven3
Secret-text credential: gorest-api-token
```

Install the Allure Jenkins plugin before using the `allure` pipeline step.
