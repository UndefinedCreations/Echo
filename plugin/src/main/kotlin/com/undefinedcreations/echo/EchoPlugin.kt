package com.undefinedcreations.echo

import com.undefinedcreations.echo.dependency.BuildToolsManager
import com.undefinedcreations.echo.dependency.RemapDependenciesExtension
import com.undefinedcreations.echo.tasks.ClearCacheTask
import com.undefinedcreations.echo.tasks.RemapTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class EchoPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        project = target

        target.tasks.register("remap", RemapTask::class.java) { task ->
            task.group = "echo"
            task.description = "This task will remap your NMS project"
        }.get().also {
            target.tasks.named("jar").get().finalizedBy(it)
        }

        target.tasks.register("clearCache", ClearCacheTask::class.java) { task ->
            task.group = "echo"
            task.description = "This task will clear the cache of build tools"
        }

        target.dependencies.extensions.create("echo", RemapDependenciesExtension::class.java, target.dependencies)

        target.repositories.mavenLocal()

        setupBuildTools()
    }

    private fun setupBuildTools() {
        BuildToolsManager.createUndefinedFolder()
        BuildToolsManager.checkBuildToolsAndInstall()
    }

    companion object {
        lateinit var project: Project
        var minecraftVersion: String? = null
    }

}

fun info(message: String) = println(message)