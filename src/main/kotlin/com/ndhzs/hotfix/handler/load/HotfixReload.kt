package com.ndhzs.hotfix.handler.load

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.handler.suffix.IHotfixSuffixHandler
import net.mamoe.mirai.console.command.CommandSender
import okio.IOException
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * 实现热修文件加载功能的工具类
 */
internal object HotfixReload {

  suspend fun CommandSender.reloadFiles(
    hotfixPlugin: HotfixKotlinPlugin,
    files: Array<File>,
    hotfixRunningFile: File,
    hotfixTypeHandlerBySuffix: Map<String, IHotfixSuffixHandler>,
    runningHotfixByFileName: Map<String, HotfixKotlinPlugin.HotfixFile>,
  ): ReloadState? {
    if (files.isEmpty()) {
      return null
    }
    val reloadState = ReloadState()
    val allowLoadFileList = mutableListOf<File>() // 记录允许进行加载的文件 list，包含被成功卸载的文件
    reloadState.removeState = HotfixRemove.run {
      removeFiles(
        files,
        runningHotfixByFileName
      )
    }?.apply {
      allowLoadFileList.addAll(
        files.filter { file ->
          removeSuccessList.any { it.file.name == file.name }
        }
      )
      allowLoadFileList.addAll(noLoadList)
    }

    for (file in allowLoadFileList) {
      val suffix = file.name.substringAfterLast(".")
      val typeHandler = hotfixTypeHandlerBySuffix[suffix]
      if (typeHandler == null) {
        reloadState.noHandlerList.add(file)
        continue
      }
      val runningFile = File(hotfixRunningFile, file.name)
      try {
        // 移动到专门运行的文件夹后再加载
        Files.move(file.toPath(), runningFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        try {
          typeHandler.apply {
            onFixLoad(runningFile, hotfixPlugin.javaClass.classLoader) // 让处理者进行加载文件
          }
          reloadState.reloadSuccessList.add(HotfixKotlinPlugin.HotfixFile(runningFile, typeHandler))
        } catch (e: ClassNotFoundException) {
          e.printStackTrace()
          reloadState.readFailureList.add(file) // 这里是加载失败的时候
          // 失败了就移动回去
          Files.move(runningFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
      } catch (e: IOException) {
        e.printStackTrace()
        reloadState.moveFailureList.add(file)
      }
    }
    return reloadState
  }

  /**
   * 每次热修加载的状态
   */
  class ReloadState {
    // 加载成功的
    val reloadSuccessList = mutableListOf<HotfixKotlinPlugin.HotfixFile>()

    // 没有处理者的，请继承 AbstractHotfixHandler 自定义处理者
    val noHandlerList = mutableListOf<File>()

    // 处理者加载失败的，可能是 ClassLoader 报的错，或者是读取报的错
    val readFailureList = mutableListOf<File>()
    
    // 移动失败
    val moveFailureList = mutableListOf<File>()

    // 卸载状态
    var removeState: HotfixRemove.RemoveState? = null
  }
}