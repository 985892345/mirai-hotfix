package com.ndhzs.hotfix.hotfix.delete.impl

import com.ndhzs.hotfix.hotfix.delete.IDeleteHotfix
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
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

  override fun delete(
    sender: CommandSender,
    loadedFile: File,
    handler: AbstractHotfixSuffixHandler
  ): Exception? {
    if (!loadedFile.exists()) return null
    return try {
      if (sender.run { handler.run { onFixUnload(loadedFile) }}) {
        System.gc()
        Thread.sleep(10)
        Files.delete(loadedFile.toPath())
        null
      } else IllegalStateException("卸载文件失败")
    } catch (e: Exception) {
      e
    }
  }
}