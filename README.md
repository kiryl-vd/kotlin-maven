# kotlin-maven

A small Kotlin project built with Maven. It is a fixture repository of the `e2e-jvm` tests of the AI Orchestrator (test case TC-2206) and of the manual smoke tests. Its real JaCoCo report is the fixture `maven-kotlin.xml` of the `coverage-jacoco` module (TC-1501).

This file is for contributors who change the JVM tests or this fixture. Use [`fixtures/README.md`](../../README.md) for the overview of all fixtures. The commands to build and test are in the section "Commands for the repository profile".

## What is in it

- One REST controller, `GreetingController` (`src/main/kotlin/com/example/greeting/`), and one data class, `Greeting`. The controller keeps no state and has no application class, because no test starts a server.
- JUnit 5 unit tests in `src/test/kotlin/`. They call the controller directly, so a test run needs no Spring context and takes about one second.
- JaCoCo. The report is written in the `test` phase, so a fast test run also has it.
- ktlint (`ktlint-maven-plugin`) bound to the `verify` phase. The rules come from `.editorconfig`.
- The Maven wrapper (`mvnw`, `.mvn/wrapper/`, type `only-script`, so there is no binary in the repository).

## Versions

Chosen on 2026-10-31. They are stable releases on Maven Central. Each one has its own version line in the best-practices pack (TC-2102 checks this against `fixtures/technologies.json`).

| Tool | Version |
|---|---|
| Java (build and target) | 21 |
| Kotlin | 2.4.21 |
| Spring Boot (parent POM) | 4.1.2 |
| JUnit Jupiter | 5.14.4 (set in `pom.xml`, the Spring Boot BOM would give 6.x) |
| Maven (wrapper) | 3.9.10 |
| JaCoCo | 0.8.16 |
| ktlint Maven plugin | 3.7.1 |

## Commands for the repository profile

All commands are argument lists without shell operators (D-27). Run them in the repository root.

```yaml
languages: [kotlin]
buildTool: maven
technologies:
  - { name: kotlin, version: "2.4.21" }
  - { name: spring-boot, version: "4.1.2" }
  - { name: java, version: "21" }
commands:
  format: ./mvnw -B -q ktlint:format
  lint: ./mvnw -B -q ktlint:check
  build: ./mvnw -B -q -DskipTests package
  fastTest: ./mvnw -B test
  fullCheck: ./mvnw -B clean verify
coverageReport: target/site/jacoco/jacoco.xml
coverageFormat: jacoco
testResults: [target/surefire-reports]
generatedPaths: [target/]
```

`fullCheck` runs the tests, writes the JaCoCo report and runs ktlint. It passes on the committed state. The first run downloads the dependencies and the Maven distribution (about 1 to 2 minutes with an empty `~/.m2`).

## Coverage of the committed state

The committed tests cover all of the code except one line: the `else` branch of `when (lang)` in `greet` (unknown language). The JaCoCo model is in `packages/modules/coverage-jacoco/test/fixtures/maven-kotlin.expected.json`.

## Variant for TC-2206: new code that is not covered

`fixtures/variants/kotlin-maven/` has two patches for the scenario `jvm-feature`. Apply them with `git apply` in the root of the repository.

1. `uncovered.diff` adds the method `farewell` to `GreetingController` and no test. The build still passes, because there is no repository threshold. JaCoCo reports `farewell` with 0 of 3 lines covered (lines 46, 47 and 49 of the changed file). New code coverage is 0 %, so Gate 3 fails with a value under 90 %.
2. `covered-fix.diff` goes on top of the first patch. It adds two tests for `farewell`. Then all 3 lines and both branches are covered, and the CRAP score of `farewell` (complexity 2, coverage 100 %) is 2.

Both states were built with `./mvnw -B clean verify` (tests, JaCoCo and ktlint pass).

## About the JaCoCo report of the `coverage-jacoco` fixture

`maven-kotlin.xml` is the file `target/site/jacoco/jacoco.xml` of a clean `./mvnw -B clean verify`. One change was made by hand: the host name in the `id` of the `<sessioninfo>` element was replaced with `fixture-host`. The reader ignores that element.

## Rules for people and agents

- Keep the project small. Do not add a dependency.
- Do not commit `target/`. It is in `.gitignore`.
