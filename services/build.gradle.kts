plugins {
    id("java")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation(project(":domain"))

    // Spring
    implementation("org.springframework:spring-core:5.3.30")
    implementation("org.springframework:spring-context:5.3.30")

    //Spring test
    testImplementation("org.springframework:spring-test:5.3.30")

    // JUnit for unit testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")

    // Mockito for mocking dependencies
    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")



}

tasks.test {
    useJUnitPlatform()
}