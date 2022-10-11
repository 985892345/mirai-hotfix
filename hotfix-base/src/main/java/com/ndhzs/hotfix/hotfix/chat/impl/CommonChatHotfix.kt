package com.ndhzs.hotfix.hotfix.chat.impl

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.hotfix.chat.IChatHotfix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.file.AbsoluteFile
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/10/11 12:59
 */
class CommonChatHotfix : IChatHotfix {
  
  override suspend fun CommandSender.chat(
    plugin: HotfixKotlinPlugin,
    file: AbsoluteFile,
    notLoadedDir: File
  ): Result<File> {
    val url = file.getUrl()
    return if (url != null) {
      val client = OkHttpClient.Builder().build()
      val request = Request.Builder()
        .url(url)
        .build()
      withContext(Dispatchers.IO) {
        runCatching {
          val response = client.newCall(request).execute()
          //转化成byte数组
          val bytes = response.body!!.bytes()
          //本地文件夹目录（下载位置）
          val filePath = File(plugin.controller.fileController.notLoadedDir, file.name).toPath()
          Files.write(filePath, bytes, StandardOpenOption.CREATE).toFile()
        }
      }
    } else Result.failure(IllegalStateException("未找到名字为 ${file.name} 的文件"))
  }
}