package com.ndhzs.extentions

import com.ndhzs.utils.HotfixDependencyHandlerScope
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/6/23 16:32
 */
abstract class HotfixConfig(private val project: Project) : ExtensionAware {
  
  internal val mConfig = mutableListOf<Config>()
  
  /**
   * 创建自定义源集并引入依赖
   * @param sourceSetName 源集名字
   * @param depend 源集依赖，只会给当前添加
   */
  fun createHotfix(sourceSetName: String, depend: (HotfixDependencyHandlerScope.() -> Unit)? = null) {
    mConfig.add(Config(sourceSetName, depend))
  }
  
  class Config(
    val sourceSetName: String,
    val depend: (HotfixDependencyHandlerScope.() -> Unit)?
  )
}