package com.undefinedcreations.remapper

import org.gradle.api.Plugin
import org.gradle.api.Project

class RemappingPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register("remap", RemapTask::class.java) {

            it.group = "undefined"
            it.description = "This task will remap your NMS project"

        }.get().also {
            target.tasks.named("jar").get().finalizedBy(it)
        }

    }
}