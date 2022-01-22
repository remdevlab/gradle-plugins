package org.remdev.gradle.plugin.domain

import VersionFor

data class AppModel(
    val identifier: String,
    val propertyFileName: String = "app.property",
    val postfix: String = "",
    val includeDate: Boolean = true,
    val includeBranchName: Boolean = true,
    val includeUserName: Boolean = true,
    var versionName: String = "",
    var versionCode: Int = 0,
    var buildNumber: Int = 0,
    var major: Int = 0,
    var minor: Int = 0,
    var patch: Int = 0
) {
    fun toUniqueId() = "$identifier-$major-$minor-$patch"
}

