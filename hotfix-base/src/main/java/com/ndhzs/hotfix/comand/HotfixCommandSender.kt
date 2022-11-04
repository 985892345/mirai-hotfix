package com.ndhzs.hotfix.comand

import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.command.CommandSender
import kotlin.coroutines.CoroutineContext

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/10/28 14:46
 */

/**
 * @param commandSender 命令的发起人
 * @param coroutineScope 绑定了热修生命周期的协程作用域，会在卸载时自动取消所有子协程
 */
class HotfixCommandSender(
  val commandSender: CommandSender,
  val coroutineScope: CoroutineScope
) : CommandSender by commandSender {
  
  override val coroutineContext: CoroutineContext
    get() = coroutineScope.coroutineContext
}
