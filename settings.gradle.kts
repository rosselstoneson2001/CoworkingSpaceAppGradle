rootProject.name = "CoSpaceApp"
include("domain", "services", "api", "security")


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
