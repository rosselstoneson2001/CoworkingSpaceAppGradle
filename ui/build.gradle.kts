plugins {
    id("java")
    id("application")
    id("war")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":services"))
    implementation(project(":domain"))

    // Logger
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")

    // Spring
    implementation("org.springframework:spring-core:5.3.30")
    implementation("org.springframework:spring-context:5.3.30")
    implementation("org.springframework:spring-webmvc:5.3.30")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.springframework:spring-web:5.3.30")

    // Tomcat
    providedCompile("org.apache.tomcat.embed:tomcat-embed-core:9.0.52")

    //Spring test
    testImplementation("org.springframework:spring-test:5.3.30")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.war {
    archiveFileName = "CoSpaceApp.war"
}

application {
    mainClass.set("com.example.ui.impl.UIMain")
}

tasks.test {
    useJUnitPlatform()
}