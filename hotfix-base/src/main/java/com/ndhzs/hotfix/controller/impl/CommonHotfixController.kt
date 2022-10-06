package com.ndhzs.hotfix.controller.impl

import com.ndhzs.hotfix.controller.AbstractHotfixController
import com.ndhzs.hotfix.file.HotfixFileController
import com.ndhzs.hotfix.hotfix.cancel.ICancelHotfix
import com.ndhzs.hotfix.hotfix.cancel.impl.CommonCancelHotfix
import com.ndhzs.hotfix.hotfix.delete.IDeleteHotfix
import com.ndhzs.hotfix.hotfix.delete.impl.CommonDeleteHotfix
import com.ndhzs.hotfix.hotfix.reload.IReloadHotfix
import com.ndhzs.hotfix.hotfix.reload.impl.CommonReloadHotfix
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.rootDir
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */

/**
 * @param loadedDirName 存放已经加载文件的文件夹名字
 * @param notLoadedDirName 存在未加载文件的文件夹名字
 */
open class CommonHotfixController(
  loadedDirName: String,
  notLoadedDirName: String,
  fileController: HotfixFileController = HotfixFileController(
    loadedDir = MiraiConsole.rootDir
      .resolve("hotfix")
      .resolve(loadedDirName),
    notLoadedDir = MiraiConsole.rootDir
      .resolve("hotfix")
      .resolve(loadedDirName)
      .resolve(notLoadedDirName)
  )
) : AbstractHotfixController(fileController) {

  open val mReloadHandler: IReloadHotfix = CommonReloadHotfix()
  open val mDeleteHandler: IDeleteHotfix = CommonDeleteHotfix()
  open val mCancelHandler: ICancelHotfix = CommonCancelHotfix()

  override fun reloadHotfix(sender: CommandSender, regex: Regex): List<Pair<File, Throwable?>> {
    val notLoadedFiles = fileController.findNotLoadedFile(regex)
    return reloadHotfixInternal(sender, notLoadedFiles, fileController.loadedDir)
  }

  override fun reloadHotfix(sender: CommandSender, regex: Regex, dir: File): List<Pair<File, Throwable?>> {
    val notLoadedFiles = HotfixFileController.findFile(regex, dir)
    return reloadHotfixInternal(sender, notLoadedFiles, fileController.loadedDir)
  }

  override fun deleteHotfix(sender: CommandSender, regex: Regex): List<Pair<File, Throwable?>> {
    val deleteFiles = fileController.findLoadedFile(regex)
    return deleteHotfixInternal(sender, deleteFiles)
  }

  override fun cancelHotfix(sender: CommandSender, regex: Regex): List<Pair<File, Throwable?>> {
    val loadedFiles = fileController.findLoadedFile(regex)
    return cancelHotfixInternal(sender, loadedFiles, fileController.notLoadedDir)
  }

  override fun cancelHotfix(sender: CommandSender, regex: Regex, dir: File): List<Pair<File, Throwable?>> {
    val loadedFiles = fileController.findLoadedFile(regex)
    return cancelHotfixInternal(sender, loadedFiles, dir)
  }

  override fun listHotfix(sender: CommandSender, regex: Regex): List<File> {
    return fileController.findLoadedFile(regex)
  }


  private fun reloadHotfixInternal(
    sender: CommandSender,
    notLoadedFiles: List<File>,
    loadedDir: File,
  ): List<Pair<File, Throwable?>> {
    return notLoadedFiles.map { notLoadedFile ->
      val handler = findSuffixHandler(notLoadedFile)
      if (handler != null) {
        val result = mReloadHandler.reload(sender, notLoadedFile, loadedDir, handler)
        Pair(result.getOrElse { notLoadedFile }, result.exceptionOrNull())
      } else {
        Pair(notLoadedFile, IllegalStateException("未找到对应的 AbstractHotfixSuffixHandler"))
      }
    }
  }

  private fun deleteHotfixInternal(
    sender: CommandSender,
    deleteFiles: List<File>,
  ) : List<Pair<File, Throwable?>> {
    return deleteFiles.map { loadedFile ->
      val handler = findSuffixHandler(loadedFile)
      if (handler != null) {
        val exception = mDeleteHandler.delete(sender, loadedFile, handler)
        Pair(loadedFile, exception)
      } else {
        Pair(loadedFile, IllegalStateException("未找到对应的 AbstractHotfixSuffixHandler"))
      }
    }
  }

  private fun cancelHotfixInternal(
    sender: CommandSender,
    loadedFiles: List<File>,
    notLoadedDir: File,
  ) : List<Pair<File, Throwable?>> {
    return loadedFiles.map { loadedFile ->
      val handler = findSuffixHandler(loadedFile)
      if (handler != null) {
        val result = mCancelHandler.cancel(sender, loadedFile, notLoadedDir, handler)
        Pair(result.getOrElse { loadedFile }, result.exceptionOrNull())
      } else {
        Pair(loadedFile, IllegalStateException("未找到对应的 AbstractHotfixSuffixHandler"))
      }
    }
  }
}