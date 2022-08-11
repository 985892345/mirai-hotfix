package com.ndhzs

import com.ndhzs.extentions.HotfixConfig
import com.ndhzs.utils.HotfixDependencyHandlerScope
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import java.io.File

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/6/23 16:31
 */
class HotfixPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val hotfixConfig = target.extensions.create("hotfix", HotfixConfig::class.java, target)
    target.run {
      dependMiraiHotfix()
      createHotfix(hotfixConfig)
    }
  }
  
  private fun Project.createHotfix(hotfixConfig: HotfixConfig) {
    afterEvaluate {
      val sourceSets = extensions.getByType(SourceSetContainer::class.java)
      hotfixConfig.mConfig.forEach { config ->
        val sourceSetName = config.sourceSetName
        val depend = config.depend
        extensions.configure(SourceSetContainer::class.java) {
          // 这里会专门生成打热修代码的 sourceSets 文件夹
          it.create(sourceSetName) { sourceSet ->
            // 依赖 main 的 output
            sourceSet.compileClasspath += sourceSets.named("main").get().output
            // 依赖 main 的 compileClasspath
            sourceSet.compileClasspath += sourceSets.named("main").get().compileClasspath
            sourceSet.java.srcDirs.forEach { file ->
              file.mkdirs()
              File(file.parentFile, "kotlin").mkdirs()
            }
            sourceSet.resources.srcDirs.forEach { file -> file.mkdirs() }
          }
        }
    
        /**
         * 给 gradle 新增打热修包的任务
         * 位置在 idea  gradle 侧边栏 Tasks/hotfix/xxx 中
         * 打好的包位置在 build/libs 下
         */
        tasks.register(sourceSetName, Jar::class.java) {
          it.group = "hotfix"
          it.exclude("META-INF/**")
          it.archiveFileName.set("$sourceSetName.jar")
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
  }
  
  private fun Project.dependMiraiHotfix() {
    repositories.maven { it.setUrl("https://jitpack.io") }
    dependencies.add("implementation", "com.github.985892345:mirai-hotfix:1.2")
  }
}