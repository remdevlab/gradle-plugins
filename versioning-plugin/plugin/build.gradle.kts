plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish")
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation(gradleApi())

    testImplementation(TestingLib.JUNIT)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins {
        create(VersioningPluginCoordinates.ID) {
            id = VersioningPluginCoordinates.ID
            implementationClass = VersioningPluginCoordinates.IMPLEMENTATION_CLASS
            version = VersioningPluginCoordinates.VERSION
        }
    }
}

// Configuration Block for the Plugin Marker artifact on Plugin Central
pluginBundle {
    website = VersioningPluginBundle.WEBSITE
    vcsUrl = VersioningPluginBundle.VCS
    description = VersioningPluginBundle.DESCRIPTION
    tags = VersioningPluginBundle.TAGS

    plugins {
        getByName(VersioningPluginCoordinates.ID) {
            displayName = VersioningPluginBundle.DISPLAY_NAME
        }
    }

    mavenCoordinates {
        groupId = VersioningPluginCoordinates.GROUP
        artifactId = VersioningPluginCoordinates.ID
        version = VersioningPluginCoordinates.VERSION
    }
}

tasks.create("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}
