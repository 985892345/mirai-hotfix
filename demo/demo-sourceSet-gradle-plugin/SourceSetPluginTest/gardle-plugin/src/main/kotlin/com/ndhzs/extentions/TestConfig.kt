package com.ndhzs.extentions

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.bundling.Jar

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/6/23 23:53
 */
abstract class TestConfig(private val project: Project) : ExtensionAware {
  internal val mConfig = mutableListOf<Config>()
  
  fun addConfig(name: String) {
    mConfig.add(Config(name))
  }
  
  class Config(
    val name: String
  )
}