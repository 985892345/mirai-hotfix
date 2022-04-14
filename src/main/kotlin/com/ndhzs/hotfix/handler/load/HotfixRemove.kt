package com.ndhzs.hotfix.handler.load

import com.ndhzs.hotfix.HotfixKotlinPlugin
import kotlinx.coroutines.delay
import net.mamoe.mirai.console.command.CommandSender
import okio.IOException
import java.io.File
import java.nio.file.Files

/**
 * 实现热修文件卸载功能的工具类
 */
internal object HotfixRemove {

  suspend fun CommandSender.removeFiles(
    files: Array<out File>,
    runningHotfixByFileName: Map<String, HotfixKotlinPlugin.HotfixFile>
  ): RemoveState? {
    if (files.isEmpty()) {
      return null
    }
    val removeState = RemoveState()
    files.forEach {
      val hotfixFile = runningHotfixByFileName[it.name]
      if (hotfixFile != null) {
        try {
          if (hotfixFile.hotfixSuffixHandler.run { onFixUnload(it) }) {
            removeState.removeSuccessList.add(hotfixFile)
          } else {
            removeState.unloadFailureList.add(hotfixFile)
          }
        } catch (e: IOException) {
          e.printStackTrace()
          removeState.closeFailureList.add(hotfixFile)
        }
      } else {
        removeState.noLoadList.add(it)
      }
    }
    if (removeState.removeSuccessList.isNotEmpty()) {
      delay(100) // 延迟一会，让其他协程或线程能够有停止的时间
      System.gc() // gc 掉对文件持有的引用，不然文件无法被删除
      delay(100)
      removeState.removeSuccessList.forEach {
        try {
          Files.delete(it.file.toPath())
        } catch (e: Exception) {
          e.printStackTrace()
          removeState.deleteFailureList.add(it)
        }
      }
      removeState.removeSuccessList.removeAll(removeState.deleteFailureList)
    }
    return removeState
  }

  class RemoveState {
    // 删除成功的
    val removeSuccessList = mutableListOf<HotfixKotlinPlugin.HotfixFile>()

    // 卸载失败的，说明处理者想要卸载时文件返回 false，表示(暂)不能被卸载
    val unloadFailureList = mutableListOf<HotfixKotlinPlugin.HotfixFile>()

    // 删除失败的，说明处理者调用卸载后文件仍被持有引用，无法被垃圾回收
    val deleteFailureList = mutableListOf<HotfixKotlinPlugin.HotfixFile>()

    // 处理者卸载失败的，可能是 ClassLoader 报的错，或者是卸载时的其他错误
    val closeFailureList = mutableListOf<HotfixKotlinPlugin.HotfixFile>()

    // 没有加载的
    val noLoadList = mutableListOf<File>()
  }
}