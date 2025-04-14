plugins {
    kotlin("jvm") version "1.9.21"
    id("com.undefinedcreations.echo")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.undefinedcreation"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    echo("1.16.5", printDebug = true)
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