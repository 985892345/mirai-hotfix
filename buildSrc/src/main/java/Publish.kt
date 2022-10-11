import org.gradle.api.Project
import java.util.*

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/10 16:57
 */
abstract class Publish(private val project: Project) {
  var artifactId = "mirai"
  var description = "基于 mirai-console，用于实现简单的逻辑代码热修"

  private val properties = Properties().apply {
    load(project.rootDir.resolve("version.properties").inputStream())
  }

  var groupId = properties.getValue("group").toString()
  var version = properties.getValue("version").toString()
}