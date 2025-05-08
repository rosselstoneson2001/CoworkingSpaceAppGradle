plugins {
    id("java-library")
}

dependencies {

    implementation(project(":domain"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")

    // Spring Framework
    implementation("org.springframework:spring-context:5.3.22")
    implementation("org.springframework:spring-tx:5.3.22")

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