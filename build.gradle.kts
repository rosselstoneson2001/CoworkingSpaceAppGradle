plugins {
    id("java")
    id("application")
    id("io.spring.dependency-management") version "1.1.7"
}

val springBootVersion = "3.2.4"

group = "com.example"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    dependencies {

        // SLF4J API (Interface for logging) || Log4j2 SLF4J Binding (to use SLF4J with Log4j2 as the backend)
        implementation("org.slf4j:slf4j-api:2.0.9")
        implementation("org.apache.logging.log4j:log4j-api:2.20.0")
        implementation("org.apache.logging.log4j:log4j-core:2.20.0")
        implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")

        // Exclude conflict dependencies
        configurations.all {
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        }

    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }

}

application {
    mainClass.set("com.example.api.CoSpaceApp")
}

