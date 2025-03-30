package com.undefinedcreations.echo.dependency

import com.undefinedcreations.echo.EchoPlugin
import org.gradle.api.artifacts.dsl.DependencyHandler

abstract class RemapDependenciesExtension(
    private val dependencies: DependencyHandler
) {
    operator fun invoke(
        minecraftVersion: String,
        mojangMapping: Boolean = true,
        generateSource: Boolean = true,
        generateDocs: Boolean = true,
        printDebug: Boolean = false
    ) {
        BuildToolsManager.buildBuildTools(minecraftVersion, mojangMapping, generateSource, generateDocs, printDebug)
        dependencies.add("compileOnly", "org.spigotmc:spigot:$minecraftVersion-R0.1-SNAPSHOT${if (mojangMapping) ":remapped-mojang" else ""}")
        EchoPlugin.minecraftVersion = minecraftVersion
    }
}