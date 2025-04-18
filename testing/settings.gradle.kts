pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("../plugin")
}

rootProject.name = "testing"

include("v1_21_4", "v1_21_5", "server", "common")