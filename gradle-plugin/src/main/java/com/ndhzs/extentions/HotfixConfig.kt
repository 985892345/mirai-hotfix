package com.ndhzs.extentions

import com.ndhzs.utils.HotfixDependencyHandlerScope
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import java.io.File

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/6/23 16:32
 */
abstract class HotfixConfig(private val project: Project) : ExtensionAware {
  
  fun createHotfix(sourceSetName: String) {
    project.createHotfix(sourceSetName, null)
  }
  
  /**
   * 创建自定义源集并引入依赖
   * @param sourceSetName 源集名字
   * @param depend 源集依赖，只会给当前添加
   */
  fun createHotfix(sourceSetName: String, depend: Action<HotfixDependencyHandlerScope>) {
    project.createHotfix(sourceSetName, depend)
  }
  
  private fun Project.createHotfix(sourceSetName: String, depend: Action<HotfixDependencyHandlerScope>?) {
    val sourceSets = extensions.getByType(SourceSetContainer::class.java)
    extensions.configure(SourceSetContainer::class.java) {
      // 这里会专门生成打热修代码的 sourceSets 文件夹
      it.create(sourceSetName) { sourceSet ->
        // 依赖 main 的 output
        sourceSet.compileClasspath += sourceSets.named("main").get().output
        // 依赖 main 的 compileClasspath
        sourceSet.compileClasspath += sourceSets.named("main").get().compileClasspath
        sourceSet.java.srcDirs.forEach { file ->
          file.mkdirs()
        }
        sourceSet.resources.srcDirs.forEach { file -> file.mkdirs() }
      }
    }
  
    /**
     * 给 gradle 新增打热修包的任务
     * 位置在 idea  gradle 侧边栏 Tasks/hotfix/xxx 中
     * 打好的包位置在 build/libs 下
     */
    tasks.register("hotfix-$sourceSetName", Jar::class.java) {
      it.group = "hotfix"
      it.exclude("META-INF/**")
      it.archiveFileName.set("hotfix-$sourceSetName.jar")
      it.from(sourceSets.named(sourceSetName).get().output)
      // 增加 runtimeClasspath
      it.from(sourceSets.named(sourceSetName).get().runtimeClasspath.filter { file ->
        // 去掉与 main 中相同的 runtimeClasspath
        !sourceSets.named("main").get().runtimeClasspath.contains(file) && file.name.endsWith(".jar")
      }.map { file -> zipTree(file) }) { copySpec ->
        copySpec.exclude("module-info.class") // 去掉 module-info 文件
        copySpec.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
      }
    }
    // 设置单个源集的依赖
    depend?.execute(HotfixDependencyHandlerScope(sourceSetName, dependencies))
  }
}