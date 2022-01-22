plugins {
    java
    id("org.remdev.versioning")
    id("com.ncorti.kotlin.gradle.template.plugin")
}

versioning {
    init(
        mutableListOf(
            VersionFor(identifier = "android-app-dev"),
            VersionFor(identifier = "android-app-prod", "prodapp.properties"),
            VersionFor(identifier = "android-app-prefix", "prefixapp.properties", "prefix")
        )
    )
}

templateExampleConfig {
    tag.set(versioning.getVersionNameFor("android-app-dev"))
    message.set(versioning.getVersionNameFor("android-app-prod"))
}
