package com.undefinedcreations.remapper

import org.gradle.api.Plugin
import org.gradle.api.Project

class RemappingPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        project = target

        target.tasks.register("remap", RemapTask::class.java) { task ->
            task.group = "undefined"
            task.description = "This task will remap your NMS project"
        }.get().also {
            target.tasks.named("jar").get().finalizedBy(it)
        }

        target.dependencies.extensions.create("remap", RemapDependenciesExtension::class.java, target.dependencies)

        setupBuildTools()
    }

    private fun setupBuildTools() {
        BuildToolsManager.createUndefinedFolder()
        BuildToolsManager.checkBuildToolsAndInstall()
    }

    companion object {
        lateinit var project: Project
    }

}