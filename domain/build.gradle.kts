plugins {
    id("java")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // SLF4J API (Interface for logging)
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Log4j2 Core (contains main functionality)
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")

    // Log4j2 SLF4J Binding (to use SLF4J with Log4j2 as the backend)
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")

    // Jackson Databind for JSON serialization/deserialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    // Jackson Datatype for Java 8 Date and Time support
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")

    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")

}

tasks.test {
    useJUnitPlatform()
}