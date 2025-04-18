plugins {
    java
    id("com.undefinedcreations.echo")
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    echo("1.21.5", printDebug = true)
    compileOnly(project(":common"))
}