package com.undefinedcreations.remapper

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.io.FileOutputStream
import java.net.URI

object BuildToolsManager {

    private val buildToolsAPI = URI.create("https://hub.spigotmc.org/jenkins/job/buildtools/api/json")
    private val gradleHome = RemappingPlugin.project.gradle.gradleUserHomeDir
    private val undefinedFolder = File(gradleHome, "undefined")
    private val buildToolsVersionFile = File(undefinedFolder, "buildToolsVersion.json")

    private val buildToolsJAR = File(undefinedFolder, "BuildTools.jar")

    fun createUndefinedFolder() { undefinedFolder.mkdirs() }

    fun hasInstalledMinecraftVersion(version: String): Boolean {
        return false
    }

    /**
     * Gets the latest stable build tools version from the Spigot Jenkins.
     */
    fun getLastBuildToolsVersionInfo(): BuildToolsVersionInfo {
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
    fun installBuildTools() {
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
    fun getLocalBuildToolsVersion(): Int {
        if (!buildToolsVersionFile.exists()) return -1
        return JsonParser.parseString(buildToolsVersionFile.readText()).asJsonObject["buildToolsVersion"].asInt
    }

    fun setLocalBuildToolsVersion(version: Int) {
        val json = JsonObject()
        json.addProperty("buildToolsVersion", version)
        buildToolsVersionFile.writeText(json.toString())
    }

    fun checkBuildToolsAndInstall() {
        val lastVersion = getLastBuildToolsVersionInfo()
        if (getLocalBuildToolsVersion() < lastVersion.version) {
            println("Installing BuildTools - ${lastVersion.version}")
            installBuildTools()
            println("Finished installing BuildTools")
            setLocalBuildToolsVersion(lastVersion.version)
        }
    }

}