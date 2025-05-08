plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4"
}

dependencies {

    implementation(project(":services"))
    implementation(project(":domain"))
    implementation(project(":security"))

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Jakarta
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.glassfish:jakarta.el:4.0.2")

    // Postgresql and jdbc connection pool
    implementation("com.zaxxer:HikariCP:5.0.1")
    runtimeOnly("org.postgresql:postgresql")

    //    Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Model Mapper
    implementation("org.modelmapper:modelmapper:3.1.1")

    // Tests
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.mockito:mockito-junit-jupiter:4.0.0")
    testImplementation ("org.springframework.security:spring-security-test")
    testImplementation ("org.springframework.boot:spring-boot-starter-web")

    testImplementation("jakarta.validation:jakarta.validation-api:3.0.2")
    testImplementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
    testImplementation("org.glassfish:jakarta.el:4.0.2")

}

tasks.test {
    useJUnitPlatform()
}
