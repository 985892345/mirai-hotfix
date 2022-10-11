package com.ndhzs.hotfix.hotfix.reload

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.hotfix.delete.IDeleteHotfix
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import net.mamoe.mirai.console.command.CommandSender
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
interface IReloadHotfix {

  /**
   * 加载热修包
   * @param notLoadedFile 未加载的文件
   * @param loadedDir 存放已经加载文件的文件夹
   * @return 返回加载后的文件，如果返回 null 则说明加载失败
   */
  suspend fun CommandSender.reload(
    plugin: HotfixKotlinPlugin,
    notLoadedFile: File,
    loadedDir: File,
    handler: AbstractHotfixSuffixHandler
  ) : Result<File>
}