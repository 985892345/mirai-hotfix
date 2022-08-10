package com.ndhzs.utils

import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/6/23 16:33
 */
class HotfixDependencyHandlerScope(
  val sourceSetName: String,
  val dependencies: DependencyHandler
) {
  fun implementation(
    dependencyNotation: Any
  ) = dependencies.add("${sourceSetName}Implementation", dependencyNotation)
  fun implementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
  ) {
    (dependencies.create(dependencyNotation) as ExternalModuleDependency).also {
      dependencyConfiguration.execute(it)
      dependencies.add("${sourceSetName}Implementation", it)
    }
  }
  fun compileOnly(
    dependencyNotation: Any
  ) = dependencies.add("${sourceSetName}CompileOnly", dependencyNotation)
  fun compileOnly(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
  ) {
    (dependencies.create(dependencyNotation) as ExternalModuleDependency).also {
      dependencyConfiguration.execute(it)
      dependencies.add("${sourceSetName}CompileOnly", it)
    }
  }
  
  fun runtimeOnly(
    dependencyNotation: Any
  ) = dependencies.add("${sourceSetName}RuntimeOnly", dependencyNotation)
  
  fun runtimeOnly(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
  ) {
    (dependencies.create(dependencyNotation) as ExternalModuleDependency).also {
      dependencyConfiguration.execute(it)
      dependencies.add("${sourceSetName}RuntimeOnly", it)
    }
  }
}