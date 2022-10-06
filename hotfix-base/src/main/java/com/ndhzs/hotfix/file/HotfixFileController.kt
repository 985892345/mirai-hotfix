package com.ndhzs.hotfix.file

import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
class HotfixFileController(
  val loadedDir: File,
  val notLoadedDir: File
) {

  /**
   * 得到已加载的文件
   */
  fun findLoadedFile(regex: Regex): List<File> {
    return findFile(regex, loadedDir)
  }

  /**
   * 得到未加载的文件
   */
  fun findNotLoadedFile(regex: Regex): List<File> {
    return findFile(regex, notLoadedDir)
  }

  companion object {
    /**
     * 查找文件
     */
    fun findFile(regex: Regex, dir: File): List<File> {
      if (!dir.exists()) {
        dir.mkdirs()
      }
      if (!dir.isDirectory) {
        return emptyList()
      }
      val children = dir.listFiles()?.toList() ?: emptyList()
      return children.filter {
        it.name.matches(regex)
      }
    }
  }
}