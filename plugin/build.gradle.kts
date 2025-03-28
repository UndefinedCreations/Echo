plugins {
    kotlin("jvm") version "1.9.21"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.undefinedcreations"
version = "1.1.16"

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    implementation("net.md-5:SpecialSource:1.11.4")
    implementation("com.google.code.gson:gson:2.12.1")
}


gradlePlugin {
    website.set("https://discord.undefinedcreation.com/")
    vcsUrl.set("https://github.com/UndefinedCreation/UndefinedRemapper")

    plugins {
        create("mapper") {
            id = "com.undefinedcreations.mapper"
            displayName = "Undefined mapper"
            description = "This gradle plugin will remapped you NMS projects."
            tags = listOf("spigot", "mapping", "NMS", "mojang", "utils", "remapper")
            implementationClass = "com.undefinedcreations.remapper.RemappingPlugin"
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier = ""
    }
}