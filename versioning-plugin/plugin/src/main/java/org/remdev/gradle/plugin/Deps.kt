package org.remdev.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.remdev.gradle.plugin.controller.VersionsController

internal object Deps {
    const val GRADLE_GROUP = "versioning"
    const val TAG = "VERSIONING: "
    const val EXTERNAL_STORAGE_PROPERTY_FILE_NAME = "build_numbers.properties"
    var logger = Logging.getLogger("org.remdev.versioning")
    lateinit var project: Project
    lateinit var extension: VersioningExtension
    lateinit var controller: VersionsController
}
