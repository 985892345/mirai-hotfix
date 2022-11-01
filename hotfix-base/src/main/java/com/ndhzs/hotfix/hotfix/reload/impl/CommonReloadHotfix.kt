package com.ndhzs.hotfix.hotfix.reload.impl

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.hotfix.reload.IReloadHotfix
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
open class CommonReloadHotfix : IReloadHotfix {
  
  override suspend fun CommandSender.reload(
    plugin: HotfixKotlinPlugin,
    notLoadedFile: File,
    loadedDir: File,
    handler: AbstractHotfixSuffixHandler
  ): Result<File> {
    if (!notLoadedFile.exists()) return Result.failure(IllegalStateException("需要加载的文件不存在"))
    if (!loadedDir.exists()) return Result.failure(IllegalStateException("存放已经加载文件的文件夹不存在"))
    if (notLoadedFile.parentFile == loadedDir) {
      // 已经在存放加载文件的文件夹中时
      return if (notLoadedFile.canRead() && notLoadedFile.canWrite()) {
        val loadException = loadFile(plugin, notLoadedFile, handler)
        if (loadException == null) {
          Result.success(notLoadedFile)
        } else {
          Result.failure(loadException)
        }
      } else Result.failure(IllegalStateException("需要加载的文件不可读或不可写"))
    }
    if (!notLoadedFile.canRead() || !notLoadedFile.canWrite()) {
      return Result.failure(IllegalStateException("需要加载的文件不可读或不可写"))
    }
    val loadedFile = loadedDir.resolve(notLoadedFile.name)
    // 需要先删除已经加载了的
    val deleteException = deleteLoadedFile(plugin, loadedFile, handler)
    if (deleteException != null) {
      return Result.failure(deleteException)
    }
    try {
      // 移动到专门运行的文件夹后再加载
      withContext(Dispatchers.IO) {
        Files.move(notLoadedFile.toPath(), loadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
      }
      // 加载文件
      val loadException = loadFile(plugin, loadedFile, handler)
      if (loadException != null) {
        // 失败了就移动回去
        withContext(Dispatchers.IO) {
          Files.move(loadedFile.toPath(), notLoadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        return Result.failure(loadException)
      }
    } catch (e: Exception) {
      return Result.failure(e)
    }
    return Result.success(loadedFile)
  }
  
  private suspend fun CommandSender.deleteLoadedFile(
    plugin: HotfixKotlinPlugin,
    loadedFile: File,
    suffix: AbstractHotfixSuffixHandler
  ): Exception? {
    if (!loadedFile.exists()) return null
    return try {
      if (suffix.onFixUnloadInternal(this, plugin, loadedFile)) {
        System.gc()
        delay(20)
        withContext(Dispatchers.IO) {
          Files.delete(loadedFile.toPath())
        }
        null
      } else IllegalStateException("已经加载的文件中存在同名文件，但不允许卸载")
    } catch (e: Exception) {
      e
    }
  }
  
  private suspend fun CommandSender.loadFile(
    plugin: HotfixKotlinPlugin,
    file: File,
    suffix: AbstractHotfixSuffixHandler
  ): Exception? {
    return try {
      suffix.onFixLoadInternal(this, plugin, file)
      null
    } catch (e: Exception) {
      e
    }
  }
}