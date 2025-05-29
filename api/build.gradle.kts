plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4"
}

dependencies {

    implementation(project(":services"))
    implementation(project(":domain"))

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Jakarta
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")

    // Postgresql and jdbc connection pool
    implementation("com.zaxxer:HikariCP:5.0.1")
    runtimeOnly("org.postgresql:postgresql")

    //    Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")


    // Tests
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.mockito:mockito-junit-jupiter:4.0.0")
    testImplementation ("org.springframework.security:spring-security-test")
    testImplementation ("org.springframework.boot:spring-boot-starter-web")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
        exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j2-impl")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }

}

tasks.test {
    useJUnitPlatform()
}
