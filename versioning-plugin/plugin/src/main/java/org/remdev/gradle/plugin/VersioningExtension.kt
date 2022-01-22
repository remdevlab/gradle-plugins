package org.remdev.gradle.plugin

import VersionFor
import org.gradle.api.Project
import org.remdev.gradle.plugin.Deps.logger
import org.remdev.gradle.plugin.domain.AppModel
import javax.inject.Inject

@Suppress("UnnecessaryAbstractClass")
abstract class VersioningExtension @Inject constructor(val project: Project) {
    val artifacts: MutableList<AppModel> = mutableListOf()

    fun getVersionNameFor(alias: String): String {
        logger.lifecycle("${Deps.TAG} get version name for $alias")
        return artifacts.firstOrNull { it.identifier == alias }?.versionName ?: "error"
    }

    fun getVersionCodeFor(alias: String): Int {
        logger.lifecycle("${Deps.TAG} get version code for $alias")
        return artifacts.firstOrNull { it.identifier == alias }?.buildNumber ?: 1
    }

    fun init(apps: MutableList<VersionFor>) {
        logger.lifecycle("${Deps.TAG} init start")
        Deps.controller.initDefaultVersions(apps, artifacts)
        logger.lifecycle("${Deps.TAG} init end")
    }
}
