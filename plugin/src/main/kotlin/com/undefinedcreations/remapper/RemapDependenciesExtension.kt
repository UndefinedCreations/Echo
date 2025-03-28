package com.undefinedcreations.remapper

import org.gradle.api.artifacts.dsl.DependencyHandler

abstract class RemapDependenciesExtension(
    private val dependencies: DependencyHandler
) {

    fun buildTools(minecraftVersion: String) {

    }

}