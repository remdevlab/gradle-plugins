package org.remdev.gradle.plugin.controller.impl

import VersionFor
import org.gradle.api.Project
import org.remdev.gradle.plugin.Deps
import org.remdev.gradle.plugin.controller.VersionsController
import org.remdev.gradle.plugin.domain.AppModel
import toAppModel
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FileVersionController : VersionsController {

    private val project: Project by lazy { Deps.project }
    private val externalStorage = File(System.getProperty("user.home"), ".gradle/${Deps.EXTERNAL_STORAGE_PROPERTY_FILE_NAME}")

    override fun initDefaultVersions(variants: List<VersionFor>, artifacts: MutableList<AppModel>) {
        initExternalStorage()
        Deps.logger.warn(Deps.TAG + "Variants to artifacts")
        for (it in variants) {
            artifacts.add(initArtifact(it))
        }
        Deps.logger.warn(Deps.TAG + "Done")
    }

    private fun initExternalStorage() {
        Deps.logger.warn(Deps.TAG + "Init external storage: ${externalStorage.exists()}, ${externalStorage.absolutePath}")
        if (!externalStorage.exists()) {
            createEmptyPropertyFile(externalStorage)
        }
    }

    private fun createEmptyPropertyFile(file: File) {
        FileWriter(file).use { writer ->
            writer.flush()
            Deps.logger.warn(Deps.TAG + "Property file flushed")
        }
    }

    private fun initArtifact(it: VersionFor): AppModel {
        Deps.logger.warn(Deps.TAG + "Init artifact: ${it.identifier}")
        val result = it.toAppModel()
        val versionFile = File(project.rootDir, result.propertyFileName)
        if (!versionFile.exists()) {
            createEmptyPropertyFile(versionFile)
            populateNewVersionFileWithEmptyValues(versionFile)
        }
        readGeneralVersions(result, versionFile)
        result.buildNumber = readBuildNumber(result.toUniqueId())
        result.versionCode = buildVersionCode(result)
        result.versionName = buildVersionName(result)
        return result
    }

    private fun buildVersionName(result: AppModel): String {
        val id = result.toUniqueId()
        var versionName = "${result.major}.${result.minor}.${result.patch}.${result.buildNumber}"
        if (result.includeDate) {
            versionName += "_${SimpleDateFormat("MMddyy_HHmm").format(Date())}"
        }
        if (result.includeBranchName) {
            versionName += "_${"git rev-parse --abbrev-ref HEAD".runCommand(project.rootDir)}"
        }
        if (result.includeUserName) {
            versionName += "_${System.getProperty("user.name")}"
        }
        if (result.postfix.isNotEmpty()) {
            versionName += "_${result.postfix}"
        }
        Deps.logger.warn(Deps.TAG + "Version name of $id is: $versionName")
        return versionName
    }

    private fun buildVersionCode(result: AppModel): Int {
        val code = result.major * MAJOR_FACTOR + result.minor * MINOR_FACTOR + result.patch * PATCH_FACTOR + result.buildNumber
        Deps.logger.warn(Deps.TAG + "Version code of ${result.toUniqueId()} is: $code")
        return code
    }

    private fun readBuildNumber(id: String): Int {
        val props = Properties()
        FileInputStream(externalStorage).use { props.load(it) }
        return props.getOrDefault(id, ONE).toString().toInt()
    }

    private fun readGeneralVersions(model: AppModel, versionFile: File) {
        val props = Properties()
        FileInputStream(versionFile).use { props.load(it) }
        model.major = props.getProperty(MAJOR, ZERO.toString()).toInt()
        model.minor = props.getProperty(MINOR, ZERO.toString()).toInt()
        model.patch = props.getProperty(PATCH, ZERO.toString()).toInt()
    }

    private fun populateNewVersionFileWithEmptyValues(versionFile: File) {
        val props = Properties()
        FileInputStream(versionFile).use { props.load(it) }

        props.setProperty(MAJOR, ZERO.toString())
        props.setProperty(MINOR, ZERO.toString())
        props.setProperty(PATCH, ONE.toString())

        flushToFile(versionFile, props)
    }

    override fun incrementVersionFor(model: AppModel) {
        val props = Properties()
        FileInputStream(externalStorage).use { props.load(it) }
        model.buildNumber += 1
        props.setProperty(model.toUniqueId(), model.buildNumber.toString())
        flushToFile(externalStorage, props)
    }

    private fun flushToFile(file: File, props: Properties) {
        FileWriter(file).use { writer ->
            try {
                props.store(writer, "This file contains Application version [MAJOR.MINOR.PATCH]")
                writer.flush()
                Deps.logger.warn(Deps.TAG + "Property file flushed")
            } catch (th: IOException) {
                Deps.logger.warn(Deps.TAG + th.toString())
            }
            Deps.logger.warn(Deps.TAG + "Property file flushed")
        }
    }

    companion object {
        const val ZERO = 0
        const val ONE = 1
        const val MAJOR = "MAJOR"
        const val MAJOR_FACTOR = 10_000_000
        const val MINOR = "MINOR"
        const val MINOR_FACTOR = 100_000
        const val PATCH = "PATCH"
        const val PATCH_FACTOR = 1000
        private const val AWAITING_TIMEOUT = 10L

        fun String.runCommand(workingDir: File): String {
            try {
                val parts = this.split("\\s".toRegex())
                val proc = ProcessBuilder(*parts.toTypedArray())
                    .directory(workingDir)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()

                proc.waitFor(AWAITING_TIMEOUT, TimeUnit.SECONDS)
                return proc.inputStream.bufferedReader().readText()
            } catch (e: IOException) {
                Deps.logger.warn(Deps.TAG + e.toString(), e)
                return "nogit"
            }
        }
    }
}
