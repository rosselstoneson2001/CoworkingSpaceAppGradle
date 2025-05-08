plugins {
    id("java-library")
}

dependencies {

    // Jackson Databind for JSON serialization/deserialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")


    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Encryption
    implementation("org.mindrot:jbcrypt:0.4")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.0")

}

tasks.test {
    useJUnitPlatform()
}
