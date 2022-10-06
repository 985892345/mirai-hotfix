package com.ndhzs.hotfix.hotfix.delete

import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import net.mamoe.mirai.console.command.CommandSender
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
interface IDeleteHotfix {

  /**
   * 删除热修包
   * @param loadedFile 已经加载的文件
   * @return 失败时返回异常
   */
  fun delete(
    sender: CommandSender,
    loadedFile: File,
    handler: AbstractHotfixSuffixHandler
  ) : Exception?
}