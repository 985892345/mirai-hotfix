package com.ndhzs.hotfix.controller.impl

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.controller.AbstractHotfixController
import com.ndhzs.hotfix.file.HotfixFileController
import com.ndhzs.hotfix.hotfix.cancel.ICancelHotfix
import com.ndhzs.hotfix.hotfix.cancel.impl.CommonCancelHotfix
import com.ndhzs.hotfix.hotfix.chat.IChatHotfix
import com.ndhzs.hotfix.hotfix.chat.impl.CommonChatHotfix
import com.ndhzs.hotfix.hotfix.delete.IDeleteHotfix
import com.ndhzs.hotfix.hotfix.delete.impl.CommonDeleteHotfix
import com.ndhzs.hotfix.hotfix.reload.IReloadHotfix
import com.ndhzs.hotfix.hotfix.reload.impl.CommonReloadHotfix
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.console.rootDir
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.FileMessage
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */

/**
 * @param hotfixDirName 热修文件存放的总文件夹名称。该文件放在控制台根目录下
 * @param loadedDir 返回存放已经加载文件的文件夹名字，receiver 为 hotfixDirName 对应的 File
 * @param notLoadedDir 返回存在未加载文件的文件夹名字，receiver 为 hotfixDirName 对应的 File
 */
@Suppress("SuspendFunctionOnCoroutineScope")
open class CommonHotfixController(
  hotfixDirName: String,
  loadedDir: File.() -> File,
  notLoadedDir: File.() -> File,
) : AbstractHotfixController(
  HotfixFileController(
    loadedDir = MiraiConsole.rootDir
      .resolve("hotfix")
      .resolve(hotfixDirName)
      .let(loadedDir)
      .apply { mkdirs() } ,
    notLoadedDir = MiraiConsole.rootDir
      .resolve("hotfix")
      .resolve(hotfixDirName)
      .let(notLoadedDir)
      .apply { mkdirs() }
  )
) {
  
  open val mReloadHotfix: IReloadHotfix = CommonReloadHotfix()
  open val mDeleteHotfix: IDeleteHotfix = CommonDeleteHotfix()
  open val mCancelHotfix: ICancelHotfix = CommonCancelHotfix()
  open val mChatHotfix: IChatHotfix = CommonChatHotfix()
  
  override suspend fun CommandSender.reloadHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<Pair<File, Throwable?>> {
    val notLoadedFiles = fileController.findNotLoadedFile(regex)
    return reloadHotfixInternal(plugin, notLoadedFiles, fileController.loadedDir)
  }
  
  override suspend fun CommandSender.reloadHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex,
    dir: File
  ): List<Pair<File, Throwable?>> {
    val notLoadedFiles = HotfixFileController.findFile(regex, dir)
    return reloadHotfixInternal(plugin, notLoadedFiles, fileController.loadedDir)
  }
  
  override suspend fun CommandSender.deleteHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<Pair<File, Throwable?>> {
    val deleteFiles = fileController.findLoadedFile(regex)
    return deleteHotfixInternal(plugin, deleteFiles)
  }
  
  override suspend fun CommandSender.cancelHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<Pair<File, Throwable?>> {
    val loadedFiles = fileController.findLoadedFile(regex)
    return cancelHotfixInternal(plugin, loadedFiles, fileController.notLoadedDir)
  }
  
  override suspend fun CommandSender.cancelHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex,
    dir: File
  ): List<Pair<File, Throwable?>> {
    val loadedFiles = fileController.findLoadedFile(regex)
    return cancelHotfixInternal(plugin, loadedFiles, dir)
  }
  
  override suspend fun CommandSender.listHotfix(plugin: HotfixKotlinPlugin, regex: Regex): List<File> {
    return fileController.findLoadedFile(regex)
  }
  
  override suspend fun CommandSender.chatHotfix(
    plugin: HotfixKotlinPlugin,
    isWithdrawn: Boolean,
    result: suspend (List<Pair<File, Throwable?>>) -> Unit
  ) {
    if (this is MemberCommandSenderOnMessage) {
      sendMessage("请上传文件")
      globalEventChannel()
        .filterIsInstance<GroupMessageEvent>()
        .filter { it.group.id == group.id && it.sender.id == user.id }
        .subscribeOnce<GroupMessageEvent> {
          it.message
            .firstIsInstanceOrNull<FileMessage>()
            ?.toAbsoluteFile(subject)
            ?.also { file ->
              plugin.logger.info("收到聊天发来的热修文件：group: ${group.id}   file.name = ${file.name}")
              mChatHotfix.run { chat(plugin, file, fileController.notLoadedDir) }
                .onFailure { throwable ->
                  sendMessage(throwable.message ?: "加载失败")
                }.onSuccess { notLoadedFile ->
                  result.invoke(reloadHotfix(plugin, Regex(notLoadedFile.name)))
                  if (isWithdrawn) {
                    if (group.botPermission >= MemberPermission.ADMINISTRATOR) {
                      runCatching { file.delete() }
                    }
                  }
                }
            }.also { file ->
              if (file == null) {
                sendMessage("未上传群文件，热修取消")
              }
            }
        }
    } else {
      sendMessage("只支持群聊文件的热修")
    }
  }
  
  
  private suspend fun CommandSender.reloadHotfixInternal(
    plugin: HotfixKotlinPlugin,
    notLoadedFiles: List<File>,
    loadedDir: File,
  ): List<Pair<File, Throwable?>> {
    return notLoadedFiles.map { notLoadedFile ->
      val handler = findSuffixHandler(notLoadedFile)
      if (handler != null) {
        val result = mReloadHotfix.run { reload(plugin, notLoadedFile, loadedDir, handler) }
        Pair(result.getOrElse { notLoadedFile }, result.exceptionOrNull())
      } else {
        Pair(notLoadedFile, IllegalStateException("未找到对应的 AbstractHotfixSuffixHandler"))
      }
    }
  }
  
  private suspend fun CommandSender.deleteHotfixInternal(
    plugin: HotfixKotlinPlugin,
    deleteFiles: List<File>,
  ): List<Pair<File, Throwable?>> {
    return deleteFiles.map { loadedFile ->
      val handler = findSuffixHandler(loadedFile)
      if (handler != null) {
        val exception = mDeleteHotfix.run { delete(plugin, loadedFile, handler) }
        Pair(loadedFile, exception)
      } else {
        Pair(loadedFile, IllegalStateException("未找到对应的 AbstractHotfixSuffixHandler"))
      }
    }
  }
  
  private suspend fun CommandSender.cancelHotfixInternal(
    plugin: HotfixKotlinPlugin,
    loadedFiles: List<File>,
    notLoadedDir: File,
  ): List<Pair<File, Throwable?>> {
    return loadedFiles.map { loadedFile ->
      val handler = findSuffixHandler(loadedFile)
      if (handler != null) {
        val result = mCancelHotfix.run { cancel(plugin, loadedFile, notLoadedDir, handler) }
        Pair(result.getOrElse { loadedFile }, result.exceptionOrNull())
      } else {
        Pair(loadedFile, IllegalStateException("未找到对应的 AbstractHotfixSuffixHandler"))
      }
    }
  }
}