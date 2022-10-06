package com.ndhzs.hotfix

import com.ndhzs.hotfix.controller.AbstractHotfixController
import com.ndhzs.hotfix.controller.impl.CommonHotfixController
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/10 12:48
 */

/**
 * 热修插件，会额外添加热修命令
 *
 * 目前热修只能热修业务代码，对于控制台的命令这些暂不支持热修
 *
 * ```
 * - /bots
 * - /config
 * - /data
 * - /hotfix
 * --- /hotfix-demo-plugin       这是 hotfixDirName，你需要把文件部署在该目录下，再调用热修命令
 * ------ /.run                  这是热修成功正在运行的文件
 * ------ hotfix-demo-2.0.jar    这是部署后即将进行热修的文件，使用 fix... reload hotfix-demo-2.0.jar 命令进行热修
 * - /plugins
 * ```
 *
 * @param hotfixDirName 热修文件的部署目录，会在控制台根目录的 hotfix 下，默认以项目 id 后缀命名
 * @param hotfixCommandName 热修的命令，默认为项目 id 后缀，会以 "fix${hotfixCommandName}" 命名
 */
abstract class HotfixKotlinPlugin(
  description: JvmPluginDescription,
  parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
  val hotfixCommandName: String = description.id.substringAfterLast("."),
  val controller: AbstractHotfixController = CommonHotfixController(
    loadedDirName = description.id.substringAfterLast("."),
    notLoadedDirName = ".run"
  )
) : KotlinPlugin(
  description,
  parentCoroutineContext
) {
  private val hotfixCommand by lazy {
    HotfixCommand(this)
  }

  final override fun onEnable() {
    super.onEnable()
    hotfixCommand.register()
    onHotfixEnable()
  }

  open fun onHotfixEnable() {}

  final override fun onDisable() {
    super.onDisable()
    hotfixCommand.unregister()
    onHotfixDisable()
  }

  open fun onHotfixDisable() {}

  /**
   * 添加文件后缀处理者
   *
   * 建议在 init 中调用
   */
  protected fun addSuffixHandler(handler: AbstractHotfixSuffixHandler) {
    controller.addSuffixHandler(handler)
  }
}
