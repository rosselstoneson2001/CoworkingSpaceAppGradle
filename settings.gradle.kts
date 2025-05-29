rootProject.name = "CoSpaceApp"
include("domain", "services", "controllers", "security")


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
