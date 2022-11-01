package com.ndhzs.hotfix.comand

import com.ndhzs.hotfix.HotfixKotlinPlugin
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.console.command.CommandSender
import kotlin.coroutines.CoroutineContext

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/10/28 14:46
 */
class HotfixCommandSender(
  val commandSender: CommandSender,
  val hotfixKotlinPlugin: HotfixKotlinPlugin
) : CommandSender by commandSender {
  
  private val hotfixJob = SupervisorJob(hotfixKotlinPlugin.coroutineContext[Job])
  
  override val coroutineContext: CoroutineContext
    get() = hotfixJob
}
