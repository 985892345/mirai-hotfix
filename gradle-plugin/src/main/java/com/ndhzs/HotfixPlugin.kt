package com.ndhzs

import com.ndhzs.extentions.HotfixConfig
import `mirai-hotfix-version`
import org.gradle.api.Plugin
import org.gradle.api.Project

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
    val version = if (`mirai-hotfix-version`.endsWith("SNAPSHOT")) {
//      "alpha"
      `mirai-hotfix-version`
    } else `mirai-hotfix-version`
    dependencies.add(
      "implementation",
      "io.github.985892345:mirai-hotfix:${version}"
    )
  }
}