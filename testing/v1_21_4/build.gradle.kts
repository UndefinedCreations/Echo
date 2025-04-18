plugins {
    java
    kotlin("jvm") version "1.9.21"
    id("com.undefinedcreations.echo")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    echo("1.21.4", printDebug = true)
    compileOnly(project(":common"))
}

//tasks {
//    remap {
//        minecraftVersion("1.21.4")
//    }
//}