package com.undefinedcreations.echo.dependency

import org.gradle.api.artifacts.dsl.DependencyHandler

abstract class RemapDependenciesExtension(
    private val dependencies: DependencyHandler
) {
    /**
     * Builds build tools of the selected Minecraft version with the correct options.
     * Then it'll add the build tools that was build as a dependency.
     */
    operator fun invoke(
        minecraftVersion: String,
        mojangMappings: Boolean = true,
        generateSource: Boolean = true,
        generateDocs: Boolean = true,
        printDebug: Boolean = false
    ) {
        val minorVersion = minecraftVersion.split('.')[1].toInt()
        if (mojangMappings && minorVersion <= 16) throw IllegalArgumentException("Mojang mappings aren't supported on versions 1.16.5 or below.")

        BuildToolsManager.buildBuildTools(minecraftVersion, mojangMappings, generateSource, generateDocs, printDebug)
        dependencies.add("compileOnly", "org.spigotmc:spigot:$minecraftVersion-R0.1-SNAPSHOT${if (mojangMappings) ":remapped-mojang" else ""}")
    }
}