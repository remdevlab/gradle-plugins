object VersioningPluginCoordinates {
    const val ID = "org.remdev.versioning"
    const val GROUP = "org.remdev.gradle.plugin"
    const val VERSION = "1.0.0"
    const val IMPLEMENTATION_CLASS = "org.remdev.gradle.plugin.VersioningPlugin"
}

object VersioningPluginBundle {
    const val VCS = "https://github.com/remdevlab/gradle-plugins"
    const val WEBSITE = "https://github.com/remdevlab/gradle-plugins"
    const val DESCRIPTION = "Plugin provides ability to manage versions of android applications and calculates incremental build number for each version"
    const val DISPLAY_NAME = "Android versioning"
    val TAGS = listOf(
        "versioning",
        "android",
    )
}

