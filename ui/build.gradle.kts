plugins {
    id("java")
    id("application")

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

    // Spring
    implementation("org.springframework:spring-core:5.3.30")
    implementation("org.springframework:spring-context:5.3.30")

    //Spring test
    testImplementation("org.springframework:spring-test:5.3.30")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")




}

application {
    mainClass.set("com.example.ui.impl.UIMain")
}


tasks.test {
    useJUnitPlatform()
}