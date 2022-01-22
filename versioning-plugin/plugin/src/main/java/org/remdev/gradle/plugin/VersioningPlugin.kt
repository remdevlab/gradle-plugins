package org.remdev.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.remdev.gradle.plugin.controller.impl.FileVersionController

abstract class VersioningPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("start plugin, for project: " + project.displayName)
        println("start plugin, for root: " + project.rootProject.displayName)
        Deps.controller = FileVersionController()
        Deps.project = project
        Deps.extension = project.extensions.create(EXTENSION_NAME, VersioningExtension::class.java, project)

        project.tasks.register(TASK_NAME, IncrementVersionTask::class.java) {
            it.models = Deps.extension.artifacts
        }
    }

    companion object {
        const val EXTENSION_NAME = "versioning"
        const val TASK_NAME = "incrementVersions"
    }
}
