plugins {
    java
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(8)
}