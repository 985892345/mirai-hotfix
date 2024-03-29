package com.ndhzs.hotfix.hotfix.cancel.impl

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.hotfix.cancel.ICancelHotfix
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
open class CommonCancelHotfix : ICancelHotfix {

  override suspend fun CommandSender.cancel(
    plugin: HotfixKotlinPlugin,
    loadedFile: File,
    notLoadedDir: File,
    handler: AbstractHotfixSuffixHandler
  ): Result<File> {
    if (!loadedFile.exists()) return Result.failure(IllegalStateException("已经加载的文件不存在"))
    if (!notLoadedDir.exists()) return Result.failure(IllegalStateException("存放未加载文件的文件夹不存在"))
    // 卸载文件
    val unloadException = unloadLoadedFile(plugin, loadedFile, handler)
    if (unloadException != null) {
      return Result.failure(unloadException)
    }
    if (loadedFile.parentFile == notLoadedDir) {
      // 已经在存放未加载文件的文件夹中时
      return Result.success(loadedFile)
    }
    val notLoadedFile = notLoadedDir.resolve(loadedFile.name)
    return try {
      // 移动到存放未加载文件的文件夹中
      withContext(Dispatchers.IO) {
        Files.move(loadedFile.toPath(), notLoadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
      }
      Result.success(notLoadedFile)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  private suspend fun CommandSender.unloadLoadedFile(
    plugin: HotfixKotlinPlugin,
    loadedFile: File,
    handler: AbstractHotfixSuffixHandler
  ) : Exception? {
    if (!loadedFile.exists()) return null
    return try {
      if (handler.onFixUnloadInternal(this, plugin, loadedFile)) {
        delay(20)
        System.gc()
        delay(20)
        null
      } else IllegalStateException("不被允许卸载")
    } catch (e: Exception) {
      e
    }
  }
}