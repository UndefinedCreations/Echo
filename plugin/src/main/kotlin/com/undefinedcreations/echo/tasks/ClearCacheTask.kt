package com.undefinedcreations.echo.tasks

import com.undefinedcreations.echo.dependency.BuildToolsManager
import com.undefinedcreations.echo.info
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ClearCacheTask : DefaultTask() {

    @TaskAction
    fun execute() {
        info("Clearing undefined folder...")
        BuildToolsManager.echoFolder.deleteRecursively()
        info("Cleared undefined folder.")
    }

}