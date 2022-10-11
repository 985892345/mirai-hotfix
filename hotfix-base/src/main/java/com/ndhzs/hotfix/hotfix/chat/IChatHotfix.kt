package com.ndhzs.hotfix.hotfix.chat

import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.file.AbsoluteFile
import java.io.File

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/10/11 12:53
 */
interface IChatHotfix {
  
  /**
   * @param file QQ 里的文件
   * @param notLoadedDir 存放未加载文件的文件夹
   */
  suspend fun CommandSender.chat(
    plugin: HotfixKotlinPlugin,
    file: AbsoluteFile,
    notLoadedDir: File,
  ) : Result<File>
}