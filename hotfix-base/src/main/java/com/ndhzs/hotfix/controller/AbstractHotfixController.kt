package com.ndhzs.hotfix.controller

import com.ndhzs.hotfix.file.HotfixFileController
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import com.ndhzs.hotfix.suffix.jar.JarHotfixSuffixHandler
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
abstract class AbstractHotfixController(
  val fileController: HotfixFileController
) : IHotfixController {

  private val mHotfixSuffixHandlers = arrayListOf<AbstractHotfixSuffixHandler>()

  /**
   * 添加文件后缀处理器
   */
  fun addSuffixHandler(handler: AbstractHotfixSuffixHandler) {
    mHotfixSuffixHandlers.add(handler)
  }

  /**
   * 寻找对应文件的后缀处理器
   */
  fun findSuffixHandler(file: File): AbstractHotfixSuffixHandler? {
    val suffix = file.name.substringAfterLast(".")
    mHotfixSuffixHandlers.forEach {
      if (it.typeSuffix == suffix) {
        return it
      }
    }
    // 最后使用默认的 jar 处理器
    if (suffix == "jar") {
      return JarHotfixSuffixHandler
    }
    return null
  }
}