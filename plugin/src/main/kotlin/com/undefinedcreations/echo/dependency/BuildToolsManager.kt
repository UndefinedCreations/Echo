package com.undefinedcreations.echo.dependency

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jeff_media.javafinder.JavaFinder
import com.undefinedcreations.echo.EchoPlugin
import com.undefinedcreations.echo.exceptions.UnsupportedJavaVersion
import com.undefinedcreations.echo.info
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.URI

object BuildToolsManager {

    private val buildToolsAPI = URI.create("https://hub.spigotmc.org/jenkins/job/buildtools/api/json")
    val echoFolder = File(EchoPlugin.project.gradle.gradleUserHomeDir, "undefined/echo")
    private val buildToolsVersionFile = File(echoFolder, "buildToolsVersion.json")
    private val buildToolsJAR = File(echoFolder, "BuildTools.jar")

    fun createUndefinedFolder() { echoFolder.mkdirs() }

    fun checkBuildToolsAndInstall() {
        val lastVersion = getLastBuildToolsVersionInfo()
        if (getLocalBuildToolsVersion() < lastVersion.version) {
            println("Installing BuildTools - ${lastVersion.version}")
            installBuildTools()
            println("Finished installing BuildTools")
            setLocalBuildToolsVersion(lastVersion.version)
        }
    }

    fun buildBuildTools(
        version: String,
        remapped: Boolean,
        generateSource: Boolean,
        generateDocs: Boolean,
        printDebug: Boolean
    ): File {
        val outputFolder = File(echoFolder, "$version${if (remapped) "-mojang-mapped" else ""}")
        outputFolder.mkdirs()
        val finalJar = File(outputFolder, "spigot-$version.jar")
        if (finalJar.exists()) return File(outputFolder, "spigot-$version.jar")
        getInstalledJavaVersion(version).let { if (it != -1) throw UnsupportedJavaVersion(it) }

        val command = "java -jar ${buildToolsJAR.path} --rev " +
                "$version ${if (remapped) "--remapped" else ""} " +
                "--output-dir ${outputFolder.path} --generate-docs --nogui " +
                "${if (generateSource) "--generate-source" else ""} " +
                if (generateDocs) "--generate-docs" else ""

        info("Building BuildTools... ($version)")
        runJar(command, outputFolder, printDebug)
        info("Built BuildTools. ($version)")

        return finalJar
    }

    /**
     * This method will return -1 if the correct version is installed else it will return the version needed
     */
    @Suppress("ConvertTwoComparisonsToRangeCheck")
    private fun getInstalledJavaVersion(version: String): Int {
        val installations = JavaFinder.builder().build().findInstallationsAsync().get()
        val uri = URI.create("https://hub.spigotmc.org/versions/$version.json").toURL()
        val response = JsonParser.parseString(uri.readText()).asJsonObject

        val versionsArray = response["javaVersions"].asJsonArray
        val minJava = versionsArray[0].asInt
        val maxJava = versionsArray[1].asInt

        val installedVersions = installations.map { it.version.classFileMajorVersion }

        for (installedVersion in installedVersions)
            if (minJava <= installedVersion && maxJava >= installedVersion) return -1
        return maxJava
    }

    /**
     * Gets the latest stable build tools version from the Spigot Jenkins.
     */
    private fun getLastBuildToolsVersionInfo(): BuildToolsVersionInfo {
        val response = JsonParser.parseString(buildToolsAPI.toURL().readText()).asJsonObject
        val lastStableBuild = response["lastStableBuild"].asJsonObject
        val version = lastStableBuild["number"].asInt
        return BuildToolsVersionInfo(version, URI.create("https://hub.spigotmc.org/jenkins/job/buildtools/$version/artifact/target/BuildTools.jar").toURL())
    }

    /**
     * Installs the latest build tools JAR.
     *
     * Beware: This does not set the local version after, and does not make any checks.
     */
    private fun installBuildTools() {
        val lastVersion = getLastBuildToolsVersionInfo()
        lastVersion.url.openStream().use { input ->
            FileOutputStream(buildToolsJAR).use { output ->
                input.copyTo(output)
            }
        }
    }

    /**
     * This method will return -1 if there is no version file
     */
    private fun getLocalBuildToolsVersion(): Int {
        if (!buildToolsVersionFile.exists()) return -1
        return JsonParser.parseString(buildToolsVersionFile.readText()).asJsonObject["buildToolsVersion"].asInt
    }

    private fun setLocalBuildToolsVersion(version: Int) {
        val json = JsonObject()
        json.addProperty("buildToolsVersion", version)
        buildToolsVersionFile.writeText(json.toString())
    }

    private fun runJar(command: String, outputFolder: File, print: Boolean): String {
        val processBuilder = ProcessBuilder(command.split(" "))
        processBuilder.directory(outputFolder)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        val string = StringBuilder()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                string.append(line).append("\n")
                if (print) println(line)
            }
        }
        process.waitFor()
        return string.toString()
    }

}