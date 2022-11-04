package com.ndhzs.hotfix.suffix.jar

import com.ndhzs.hotfix.comand.HotfixCommandSender
import com.ndhzs.hotfix.HotfixKotlinPlugin
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.command.ConsoleCommandSender

/**
 * 作为 jar 热修文件中启动类的接口
 *
 * 有如下规则需要注意：
 * - 实现类必须放在 jar 包根路径下，即代码必须放在 src/源集/java(kotlin) 下
 * - 一个 Jar 包里面有且只能有一个 JarEntrance 实现类
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
   * 热修加载时回调
   *
   * - suspend 为 [HotfixCommandSender.coroutineContext] 作用域下的协程，该作用域与热修文件生命进行了绑定，会在卸载时自动取消所有子协程
   * - 如果重启了控制台，则会在控制台刚加载时回调，[HotfixCommandSender.commandSender] 为 [ConsoleCommandSender]
   */
  suspend fun HotfixCommandSender.onFixLoad(plugin: HotfixKotlinPlugin)

  /**
   * 热修卸载时回调
   *
   * - suspend 为 [HotfixCommandSender.coroutineContext] 作用域下的协程，该作用域与热修文件生命进行了绑定，会在卸载时自动取消所有子协程
   *
   * 注意：你需要在这里面取消掉所有协程和线程，并移除掉其他文件中对该热修文件的引用，不然会导致无法被 gc
   *
   * @return 返回 true，则允许卸载；false 则不允许卸载
   */
  suspend fun HotfixCommandSender.onFixUnload(plugin: HotfixKotlinPlugin): Boolean
  
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

/**
 * 绑定了热修生命周期的协程作用域，会在卸载时自动取消所有子协程
 */
val JarEntrance.hotfixCoroutineScope: CoroutineScope?
  get() = JarHotfixSuffixHandler.jarByFileName
    .map { it.value }
    .find { it.entrance === this }
    ?.coroutineScope