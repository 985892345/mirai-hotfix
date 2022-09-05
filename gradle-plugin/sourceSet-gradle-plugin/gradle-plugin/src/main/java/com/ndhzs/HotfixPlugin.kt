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
    target.run {
      extensions.create("hotfix", HotfixConfig::class.java, target)
      dependMiraiHotfix()
    }
  }
  
  private fun Project.dependMiraiHotfix() {
    repositories.maven { it.setUrl("https://jitpack.io") }
    dependencies.add("implementation", "com.github.985892345:mirai-hotfix:1.3")
  }
}