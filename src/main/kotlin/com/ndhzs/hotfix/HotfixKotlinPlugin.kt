package com.ndhzs.hotfix

import com.ndhzs.hotfix.handler.suffix.IHotfixSuffixHandler
import com.ndhzs.hotfix.handler.suffix.jar.JarHotfixSuffixHandler
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import java.io.File
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
 * @param hotfixCommandName 热修的命令，会以 "fix${hotfixCommandName}" 命名
 * @param typeHandlers 热修文件的类型处理者，默认添加自带的 jar，可以实现 IHotfixHandler 接口自定义自己的热修逻辑
 */
abstract class HotfixKotlinPlugin(
  description: JvmPluginDescription,
  parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
  hotfixDirName: String = description.id.substringAfterLast("."),
  hotfixCommandName: String = hotfixDirName,
  typeHandlers: Array<IHotfixSuffixHandler> = arrayOf(JarHotfixSuffixHandler)
) : KotlinPlugin(
  description,
  parentCoroutineContext
) {
  @Suppress("LeakingThis")
  private val hotfixCommand = HotfixCommand(this, hotfixDirName, hotfixCommandName, typeHandlers)

  // 热修根目录
  protected val hotfixRootFile = hotfixCommand.hotfixRootFile
  // 热修加载目录
  protected val hotfixLoadFile = hotfixCommand.hotfixLoadFile
  // 热修运行目录
  protected val hotfixRunningFile = hotfixCommand.hotfixRunningFile

  // 热修文件全名与热修文件
  protected val runningHotfixByFileName: Map<String, HotfixFile> = hotfixCommand.runningHotfixByFileName

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
   */
  protected fun addSuffixHandler(handler: IHotfixSuffixHandler) {
    hotfixCommand.hotfixTypeHandlerBySuffix[handler.typeSuffix] = handler
  }

  class HotfixFile(
    val file: File,
    val hotfixSuffixHandler: IHotfixSuffixHandler
  )
}
