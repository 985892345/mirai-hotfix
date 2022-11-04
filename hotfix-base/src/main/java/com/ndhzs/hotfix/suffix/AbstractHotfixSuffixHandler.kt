package com.ndhzs.hotfix.suffix

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.comand.HotfixCommandSender
import net.mamoe.mirai.console.command.CommandSender
import java.io.File

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/10 14:41
 */

/**
 * @param typeSuffix 处理哪种文件的后缀。注意：不包含 “.”，比如：.jar 后缀直接写 jar 即可
 */
abstract class AbstractHotfixSuffixHandler(val typeSuffix: String) {
  
  internal suspend fun onFixLoadInternal(sender: CommandSender, plugin: HotfixKotlinPlugin, file: File) {
    sender.onFixLoad(plugin, file)
    if (plugin.isEnabled) {
      onEnable(plugin, file)
    }
  }
  
  internal suspend fun onFixUnloadInternal(sender: CommandSender, plugin: HotfixKotlinPlugin, file: File): Boolean {
    if (plugin.isEnabled) {
      onDisable(plugin, file)
    }
    return sender.onFixUnload(plugin, file)
  }

  /**
   * 加载此文件
   *
   * 如果该文件已经被加载了，会先调用 [onFixUnload] 进行卸载，所以只会在成功卸载后才会调用这个方法
   *
   * 该方法允许你抛出 [ClassNotFoundException] 异常，在调用处会统一收集异常
   *
   * @param file File 为需要热修的文件
   */
  @Throws(ClassNotFoundException::class)
  abstract suspend fun CommandSender.onFixLoad(plugin: HotfixKotlinPlugin, file: File)

  /**
   * 卸载此文件
   *
   * 在这里你应该做以下几点：
   * - 取消协程和线程
   * - 取消所有其他包中对该热修文件的引用
   *
   * @param file File 为 .run 文件夹下已加载的文件
   * @return 返回 true，表示能够卸载；返回 false，表示不允许卸载
   */
  abstract suspend fun CommandSender.onFixUnload(plugin: HotfixKotlinPlugin, file: File): Boolean
  
  /**
   * - 收到 [HotfixKotlinPlugin.onEnable] 时会回调
   * - 如果热修时 plugin 已经处于 Enabled 状态，则会立马回调该方法
   * @param file 为 null 说明是 [HotfixKotlinPlugin.onEnable] 的回调
   */
  abstract fun onEnable(plugin: HotfixKotlinPlugin, file: File?)
  
  /**
   * - 收到 [HotfixKotlinPlugin.onDisable] 时会回调
   * - 调用 [onFixUnload] 前也会回调该方法
   * @param file 为 null 说明是 [HotfixKotlinPlugin.onDisable] 的回调
   */
  abstract fun onDisable(plugin: HotfixKotlinPlugin, file: File?)
}
