plugins {
    kotlin("jvm") version "1.9.21"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.undefinedcreations"
version = "0.0.4"

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    implementation("net.md-5:SpecialSource:1.11.4")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("com.jeff-media:javafinder:1.4.4")
}


gradlePlugin {
    website = "https://discord.undefinedcreations.com/"
    vcsUrl = "https://github.com/UndefinedCreations/UndefinedRemapper"

    plugins {
        create("echo") {
            id = "com.undefinedcreations.echo"
            displayName = "Echo"
            description = "This gradle plugin will remapped you NMS projects. It can also build and import BuildTools."
            tags = listOf("spigot", "mapping", "NMS", "mojang", "utils", "remapper", "minecraft", "buildtools")
            implementationClass = "com.undefinedcreations.echo.EchoPlugin"
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier = ""
    }
}