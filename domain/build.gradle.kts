plugins {
    id("java")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // SLF4J API (Interface for logging) || Log4j2 SLF4J Binding (to use SLF4J with Log4j2 as the backend)
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Jackson Databind for JSON serialization/deserialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")

    // Postgresql and jdbc connection pool
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.7.1")

    // Encryption
    implementation ("org.mindrot:jbcrypt:0.4")

    // JPA & Hibernate
    implementation("org.springframework.data:spring-data-jpa:3.4.4")
    implementation("org.hibernate:hibernate-core:5.6.15.Final")

    // Spring
    implementation("org.springframework:spring-core:5.3.30")
    implementation("org.springframework:spring-context:5.3.30")

    //Spring test
    testImplementation("org.springframework:spring-test:5.3.30")

    // Testing Junit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")

    // Testing mockito
    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")

}

tasks.test {
    useJUnitPlatform()
}