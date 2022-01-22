package org.remdev.gradle.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertNotNull
import org.junit.Ignore
import org.junit.Test

class VersioningPluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("remdev.versioning")

        assert(project.tasks.getByName("incrementVersions") is IncrementVersionTask)
    }

    @Test
    fun `extension templateExampleConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("remdev.versioning")

        assertNotNull(project.extensions.getByName("versioning"))
    }

    @Test
    @Ignore
    fun `parameters are passed correctly from extension to task`() {
        /* val project = ProjectBuilder.builder().build()
         project.pluginManager.apply("remdev.versioning")
         val aFile = File(project.projectDir, ".tmp")
         (project.extensions.getByName("versioning") as VersioningExtension).apply {
             tag.set("a-sample-tag")
             message.set("just-a-message")
             outputFile.set(aFile)
         }

         val task = project.tasks.getByName("incrementVersions") as IncrementVersionTask

         assertEquals("a-sample-tag", task.tag.get())
         assertEquals("just-a-message", task.message.get())
         assertEquals(aFile, task.outputFile.get().asFile)*/
    }
}
