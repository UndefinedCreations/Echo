plugins {
    kotlin("jvm") version "1.9.21"
    id("com.undefinedcreations.mapper") version "1.1.16"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.undefinedcreation"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    remap.buildTools("1.12.4")
}

tasks {
    shadowJar {
        archiveFileName.set("server-1.0.0.jar")
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
    compileJava {
        options.release.set(21)
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}