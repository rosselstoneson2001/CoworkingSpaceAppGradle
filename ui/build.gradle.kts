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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.slf4j:slf4j-api:2.0.9")

    implementation(project(":services"))
    implementation(project(":domain"))
}

application {
    mainClass.set("com.example.ui.impl.UIMain")
}


tasks.test {
    useJUnitPlatform()
}