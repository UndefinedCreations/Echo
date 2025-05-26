import com.undefinedcreations.nova.ServerType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.undefinedcreations.nova") version "0.0.3"
}

group = "com.undefinedcreations"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    implementation(project(":common"))
    implementation(project(":v1_21_4"))
    implementation(project(":v1_21_5"))
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }
    compileJava {
        options.release.set(8)
    }
    runServer {
        minecraftVersion("1.21.4")
        perVersionFolder(true)
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