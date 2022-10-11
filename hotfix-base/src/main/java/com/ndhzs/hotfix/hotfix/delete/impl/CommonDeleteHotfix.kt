package com.ndhzs.hotfix.hotfix.delete.impl

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.hotfix.delete.IDeleteHotfix
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import java.io.File
import java.nio.file.Files

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
open class CommonDeleteHotfix : IDeleteHotfix {

  override suspend fun CommandSender.delete(
    plugin: HotfixKotlinPlugin,
    loadedFile: File,
    handler: AbstractHotfixSuffixHandler
  ): Exception? {
    if (!loadedFile.exists()) return null
    return try {
      if (handler.run { onFixUnloadInternal(plugin, loadedFile) }) {
        System.gc()
        delay(20)
        withContext(Dispatchers.IO) {
          Files.delete(loadedFile.toPath())
        }
        null
      } else IllegalStateException("卸载文件失败")
    } catch (e: Exception) {
      e
    }
  }
}