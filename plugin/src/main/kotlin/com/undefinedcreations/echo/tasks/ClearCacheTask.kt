package com.undefinedcreations.echo.tasks

import com.undefinedcreations.echo.dependency.BuildToolsManager
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ClearCacheTask : DefaultTask() {

    @TaskAction
    fun execute() {
        println("Clearing undefined folder...")
        BuildToolsManager.echoFolder.deleteRecursively()
        println("Cleared undefined folder.")
    }

}