plugins {
    id("java")
    id("application") // Apply the application plugin if your project has an entry point
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

group = "com.example" // Change this to your project group
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral() // Set Maven Central as a repository to resolve dependencies
}

dependencies {

    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":services"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    // Correct fully qualified name of the main class
    mainClass.set("com.example.UIMain") // Adjust the package path accordingly
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}


tasks.register<JavaExec>("runInteractive") {
    group = "application"
    mainClass.set("com.example.UIMain")
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    manifest {
        attributes["Main-Class"] = "com.example.UIMain"
    }
}

tasks.test {
    useJUnitPlatform() // Use JUnit platform to run tests
}
