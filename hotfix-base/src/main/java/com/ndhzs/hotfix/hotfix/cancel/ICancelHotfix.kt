package com.ndhzs.hotfix.hotfix.cancel

import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import net.mamoe.mirai.console.command.CommandSender
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
interface ICancelHotfix {

  /**
   * 取消热修包
   * @param loadedFile 已经加载的文件
   * @param notLoadedDir 存放未加载文件的文件夹
   */
  fun cancel(
    sender: CommandSender,
    loadedFile: File,
    notLoadedDir: File,
    handler: AbstractHotfixSuffixHandler
  ) : Result<File>
}