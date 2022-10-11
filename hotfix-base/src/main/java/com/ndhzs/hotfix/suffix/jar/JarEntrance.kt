package com.ndhzs.hotfix.suffix.jar

import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender

/**
 * 作为 jar 热修文件中启动类的接口
 *
 * 有如下规则需要注意：
 * - 实现类必须放在 jar 包根路径下，即代码必须放在 src/源集/java 下
 *
 * ## 如何通信
 * 请在需要使用的类中实现 [JarHotfixUser] 接口
 * ```
 * class HotfixUser : JarHotfixUser {
 *
 *   // 采用这种方式，防止一直持有引用而导致无法被卸载
 *   private val connect: IConnect?
 *     get() = getEntrance(IConnect::class.java)
 *
 *   // 在热修将被卸载时回调，需要在这里清除掉所有对 IConnect 的引用并返回 true
 *   override fun onRemoveEntrance(entrance: JarEntrance): Boolean {
 *     return true
 *   }
 * }
 * ```
 */
interface JarEntrance {
  
  /**
   * 热修加载时调用
   *
   * 如果重启了控制台，则会在控制台刚加载时回调，[CommandSender] 为 [ConsoleCommandSender]
   */
  suspend fun CommandSender.onFixLoad(plugin: HotfixKotlinPlugin)

  /**
   * 需要卸载时回调
   * @return 返回 true，则允许卸载；false 则不允许卸载
   */
  suspend fun CommandSender.onFixUnload(plugin: HotfixKotlinPlugin): Boolean
  
  /**
   * - 收到 [HotfixKotlinPlugin.onEnable] 时会回调
   * - 如果热修时 plugin 已经处于 Enabled 状态，则会立马回调该方法
   */
  fun onEnable(plugin: HotfixKotlinPlugin) {}
  
  /**
   * - 收到 [HotfixKotlinPlugin.onDisable] 时会回调
   * - 调用 [onFixUnload] 前也会回调该方法
   */
  fun onDisable(plugin: HotfixKotlinPlugin) {}
  
}