package com.ndhzs

import com.ndhzs.extentions.TestConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import java.io.File

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/6/23 23:51
 */
class TestPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.run {
      val testConfig = extensions.create("test", TestConfig::class.java, this)
      create(testConfig)
    }
  }
  
  private fun Project.create(testConfig: TestConfig) {
    afterEvaluate {
      testConfig.mConfig.forEach {
        val name = it.name
        extensions.getByType(SourceSetContainer::class.java).create(name) { sourceSet ->
          sourceSet.java.srcDirs.forEach { file ->
            file.mkdirs()
            File(file.parentFile, "kotlin").mkdirs()
          }
          sourceSet.resources.srcDirs.forEach { file -> file.mkdirs() }
        }
        tasks.register(name) {
          it.group = "demo"
        }
      }
    }
  }
}