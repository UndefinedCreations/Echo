pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "Echo"

includeBuild("plugin")
includeBuild("testing")