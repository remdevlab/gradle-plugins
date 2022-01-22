package org.remdev.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.remdev.gradle.plugin.domain.AppModel

abstract class IncrementVersionTask : DefaultTask() {

    init {
        description = "Task to increment versions for all defined artifacts"
        group = Deps.GRADLE_GROUP
    }

    @get:Input
    @get:Option(option = "models", description = "Artifacts to increment versions")
    var models: MutableList<AppModel> = mutableListOf()

    @TaskAction
    fun sampleAction() {
        logger.lifecycle("${Deps.TAG} start increment version")
        models.forEach {
            logger.lifecycle("${Deps.TAG} For: $it ")
            Deps.controller.incrementVersionFor(it)
        }
        logger.lifecycle("${Deps.TAG} finish increment version")
    }
}
