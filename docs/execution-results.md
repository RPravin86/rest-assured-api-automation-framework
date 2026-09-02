# Verified execution results

The following results were captured while developing and validating the framework against the GoREST QA endpoint.

## Complete sequential suite

Command:

```bash
mvn clean test
```

Verified GitHub Actions result:

```text
Starting API automation against qa (https://gorest.co.in)
Starting TestNG suite: GoREST API Automation Suite
Finished TestNG suite: GoREST API Automation Suite
[passed=55, failed=0, skipped=0]

Tests run: 55, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The run also verified:

- Java 17 compilation on an Ubuntu GitHub-hosted runner
- secure availability and masking of `GOREST_API_TOKEN`
- Allure HTML report generation
- upload of raw Allure results, Surefire reports and Log4j2 logs
- Maven dependency caching

## Parallel API suite

Command:

```bash
mvn clean test \
  -Dtest.suite=src/test/resources/testng-parallel.xml
```

Verified result:

```text
Starting API automation against qa (https://gorest.co.in)
Starting TestNG suite: GoREST Parallel API Suite
Finished TestNG suite: GoREST Parallel API Suite
[passed=17, failed=0, skipped=0]

Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The parallel suite executes integration scenarios with three TestNG method threads. Framework unit tests remain in the sequential suite because some configuration tests temporarily modify JVM system properties.

## Allure output

Every API scenario can include:

- business-readable `@Step` entries
- HTTP method and sanitized request URL
- request headers with credentials redacted
- request payload
- response status, headers and payload
- environment name, base URL, Java version and operating system
- categorized infrastructure and API-contract failures
- cleanup fixtures and retry visibility

In GitHub Actions, download the `allure-report-<run>-<attempt>` artifact, extract it, and open `index.html`. Browsers that block local report assets can serve the extracted directory. Run this command from that directory:

```bash
python3 -m http.server 8080
```

Then open `http://localhost:8080`.

Generated reports and runtime output are intentionally excluded from Git because they are build artifacts, not source files.
