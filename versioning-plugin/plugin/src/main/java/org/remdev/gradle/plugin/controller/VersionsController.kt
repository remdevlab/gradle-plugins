package org.remdev.gradle.plugin.controller

import VersionFor
import org.remdev.gradle.plugin.domain.AppModel

interface VersionsController {
    fun initDefaultVersions(variants: List<VersionFor>, artifacts: MutableList<AppModel>)
    fun incrementVersionFor(model: AppModel)
}
