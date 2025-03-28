package com.undefinedcreations.remapper

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import java.io.File
import java.nio.file.Files

import net.md_5.specialsource.Jar
import net.md_5.specialsource.JarMapping
import net.md_5.specialsource.JarRemapper
import net.md_5.specialsource.provider.JarProvider
import net.md_5.specialsource.provider.JointProvider
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import java.nio.file.StandardCopyOption

abstract class RemapTask: DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    private var minecraftVersion: String? = null
    private var action: Action = Action.MOJANG_TO_SPIGOT
    private var inputTask: Task = project.tasks.named("jar").let {
        if ("shadowJar" in project.tasks.names) {
            val shadowJar = project.tasks.named("shadowJar")
            setDependsOn(mutableListOf(shadowJar))
            return@let shadowJar.get()
        }
        setDependsOn(mutableListOf(it))
        it.get()
    }
    private var createNewJar = false

    @OutputFile
    var outFile: File = File("${project.layout.buildDirectory.get().asFile}/cache", "${project.name}-${project.version}.jar")

    fun minecraftVersion(minecraftVersion: String) { this.minecraftVersion = minecraftVersion }
    fun inputTask(task: Provider<out Task>) {
        setDependsOn(mutableListOf(task))
        inputTask = task.get()
    }
    fun action(action: Action) { this.action = action }
    fun createNewJar(newJar: Boolean) { createNewJar = newJar }

    @TaskAction
    fun execute() {
        val task = inputTask
        val archiveFile = task.outputs.files.singleFile

        val cacheFolder = File(project.layout.buildDirectory.get().asFile, "cache")
        if (!cacheFolder.exists()) cacheFolder.mkdirs()

        println("Remapping Jar....")

        val version = minecraftVersion ?: throw IllegalArgumentException("Version need to be specified for ${project.path}")

        var fromFile = archiveFile
        var tempFile = Files.createTempFile(null, ".jar").toFile()
        val action = Action.MOJANG_TO_SPIGOT
        val iterator = action.procedures.iterator()

        var shouldRemove = false

        while (iterator.hasNext()) {
            val procedures = iterator.next()
            procedures.remap(project, version, fromFile, tempFile)

            if (shouldRemove) fromFile.delete()

            if (iterator.hasNext()) {
                fromFile = tempFile
                tempFile = Files.createTempFile(null, ".jar").toFile()
                shouldRemove = true
            }
        }


        if (createNewJar) {
            val ta = File(archiveFile.parentFile, "${project.name}-remapped.jar")
            tempFile.copyTo(ta, true)
        } else {
            Files.copy(
                tempFile.toPath(),
                archiveFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
        }


        val output = outFile
        Files.copy(tempFile.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING)
        tempFile.delete()

        println("Successfully remapped!")
    }

    enum class Action(internal vararg val procedures: ActualProcedure) {
        MOJANG_TO_SPIGOT(ActualProcedure.MOJANG_OBF, ActualProcedure.OBF_SPIGOT),
        MOJANG_TO_OBF(ActualProcedure.MOJANG_OBF),
        OBF_TO_MOJANG(ActualProcedure.OBF_MOJANG),
        OBF_TO_SPIGOT(ActualProcedure.OBF_SPIGOT),
        SPIGOT_TO_MOJANG(ActualProcedure.SPIGOT_OBF, ActualProcedure.OBF_MOJANG),
        SPIGOT_TO_OBF(ActualProcedure.SPIGOT_OBF);
    }

    internal enum class ActualProcedure(
        private val mapping: (version: String) -> String,
        private val inheritance: (version: String) -> String,
        private val reversed: Boolean = false
    ) {
        MOJANG_OBF(
            { version -> "org.spigotmc:minecraft-server:$version-R0.1-SNAPSHOT:maps-mojang@txt" },
            { version -> "org.spigotmc:spigot:$version-R0.1-SNAPSHOT:remapped-mojang" },
            true
        ),
        OBF_MOJANG(
            { version -> "org.spigotmc:minecraft-server:$version-R0.1-SNAPSHOT:maps-mojang@txt" },
            { version -> "org.spigotmc:spigot:$version-R0.1-SNAPSHOT:remapped-obf" }
        ),
        SPIGOT_OBF(
            { version -> "org.spigotmc:minecraft-server:$version-R0.1-SNAPSHOT:maps-spigot@csrg" },
            { version -> "org.spigotmc:spigot:$version-R0.1-SNAPSHOT" },
            true
        ),
        OBF_SPIGOT(
            { version -> "org.spigotmc:minecraft-server:$version-R0.1-SNAPSHOT:maps-spigot@csrg" },
            { version -> "org.spigotmc:spigot:$version-R0.1-SNAPSHOT:remapped-obf" }
        );

        fun remap(project: Project, version: String, jarFile: File, outputFile: File) {
            val dependencies = project.dependencies
            val mappingFile = project.configurations.detachedConfiguration(dependencies.create(mapping(version))).singleFile
            val inheritanceFile = project.configurations.detachedConfiguration(dependencies.create(inheritance(version))).apply {
                    isTransitive = false
                }.singleFile

            Jar.init(jarFile).use { inputJar ->
                Jar.init(inheritanceFile).use { inheritanceJar ->
                    val mapping = JarMapping()
                    mapping.loadMappings(mappingFile.canonicalPath, reversed, false, null, null)
                    val provider = JointProvider()
                    provider.add(JarProvider(inputJar))
                    provider.add(JarProvider(inheritanceJar))
                    mapping.setFallbackInheritanceProvider(provider)

                    val mapper = JarRemapper(mapping)
                    mapper.remapJar(inputJar, outputFile)
                }
            }
        }
    }
}