# CoSpaceApp

CoSpaceApp is a Java-based, multi-module project built with Gradle Kotlin DSL. It consists of several modules, including domain, services, controllers, and security. The project follows SOLID principles for clean and maintainable code, incorporates Log4j2 for logging, and supports both CLI and Spring Boot execution for flexible use cases.
### `feature(branch) 9`: JPA, Core Java, and Advanced Features
- Implemented **Stream API** and **Lambdas**.
- Used **Criteria API** and **JPQL**.
- Leveraged **Core Java** concepts like **Collections**, **Generics**, and **Optional**.
- Created and utilized **custom exceptions**.
- Developed **custom loggers**.
- Migrated the project to a **multi-module Gradle setup** using **Gradle Kotlin DSL**
- Integrated **JPA**.
- Wrote **core Java tests**.
- Managed data using **JSON**.
- Integrated **JDBC**.
- Integrated **HikariCP**.

### `feature 10`: Migration to Plain Spring
- Migrate the project to Spring Framework with Criteria API and JPQL for querying.
- Remove the console UI.
- Migrate to a web application using Spring MVC.

### `main`: Spring Boot with Authentication, Role-Based Access, Docker, CI/CD Pipeline, and Design Patterns

- Built using **Spring Boot**.
- Integrated **Spring Data JPA**.
- **Authentication** and **Role-Based Authorization**.
- **Docker** support for containerization.
- Set up with a **CI/CD pipeline**.
- Applies key **Design Patterns**.

### `feature-3`: Custom Class Loader
- Developed a custom class loader in a separate project. [Link to project here](https://github.com/akkamill/LoadFromCoSpace)

## Project Structure

```
CoSpaceApp/
├── controllers/        # REST API controllers handling HTTP requests
├── domain/             # Business logic and data models
├── security/           # Security-related configurations
├── services/           # Service layer with business rules
├── build.gradle.kts    # Root Gradle build file
├── settings.gradle.kts # Multi-module Gradle settings
├── README.md   # Documentation
```

### Prerequisites

- Java 17 or later
- Gradle 8.x
- PostgreSQL
- Spring Boot 2.x or later

### Build the Project

Run the following command to build all modules:

```sh
gradle build
```

### Running the Application

Running with Docker Compose in IntelliJ:

```sh
docker compose up --build
```

To run the application from the CLI:

```sh
gradle run
```

### Running the Application with SpringBoot

```sh
gradle bootRun
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
