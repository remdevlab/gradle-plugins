import org.remdev.gradle.plugin.domain.AppModel
import java.io.Serializable

data class VersionFor(
    val identifier: String,
    val propertyFileName: String = "app.properties",
    val postfix: String = "",
    val includeDate: Boolean = true,
    val includeBranchName: Boolean = true,
    val includeUserName: Boolean = true
) : Serializable

fun VersionFor.toAppModel() = AppModel(
    identifier = identifier,
    propertyFileName = propertyFileName,
    postfix = postfix,
    includeDate = includeDate,
    includeBranchName = includeBranchName,
    includeUserName = includeUserName
)
