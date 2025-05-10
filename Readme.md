# CoSpaceApp

CoSpaceApp is a Java-based multi-module project using Gradle Kotlin DSL. It includes multiple modules such as `domain`, `services`, and `ui`. The project follows SOLID principles, uses logging with Log4j2, and supports CLI execution.

## Project Structure

```
CoSpaceApp/
├── domain/     # Business logic and data models
├── services/   # Service layer with business rules
├── ui/         # CLI user interface
├── build.gradle.kts  # Root Gradle build file
├── settings.gradle.kts  # Multi-module Gradle settings
├── README.md   # Documentation
```

### Prerequisites

- Java 17 or later
- Gradle 8.x

### Build the Project

Run the following command to build all modules:

```sh
gradle build
```

### Running the Application

To run the application from the CLI:

```sh
gradle run
```

Or, if you need interactive input support:

```sh
gradle run -q --console=plain
```

### Creating a Fat JAR

To generate a fat JAR (including dependencies), run:

```sh
gradle shadowJar
```

The JAR will be located in `build/libs/`.

### Running the Fat JAR

After building the fat JAR, you can run it using:

```sh
java -jar build/libs/CoSpaceApp-1.0-SNAPSHOT.jar
```

### Running Tests

Run all tests with:

```sh
gradle test
```
