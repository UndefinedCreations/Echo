import com.undefinedcreations.nova.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.undefinedcreations.echo")
    id("com.undefinedcreations.nova") version "0.0.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.undefinedcreations"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    echo("1.13", mojangMappings = false, printDebug = true)
}

tasks {
    remap {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("server-1.0.0.jar")
    }
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }
    compileJava {
        options.release.set(8)
    }
    runServer {
        minecraftVersion("1.13")
        serverFolderName { "run" }
        acceptMojangEula()
        serverType(ServerType.SPIGOT)
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(8)
}