package com.ndhzs.hotfix.handler.suffix.jar

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender

/**
 * 作为 jar 热修文件中启动类的接口
 *
 * 有如下规则需要注意：
 * - 实现类必须放在 jar 包根路径下
 * - 实现类的文件建议只有实现类一个 class
 */
interface JarEntrance {
  /**
   * 热修加载时调用
   *
   * 如果重启了控制台，则会在控制台刚加载时回调，[CommandSender] 为 [ConsoleCommandSender]
   */
  suspend fun CommandSender.onFixLoad()

  /**
   * 需要卸载时回调
   * @return 返回 true，则允许卸载；false 则不允许卸载
   */
  suspend fun CommandSender.onFixUnload(): Boolean
}