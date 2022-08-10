package com.ndhzs.hotfix.handler.suffix

import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.command.CommandSender
import java.io.File

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/10 14:41
 */
interface IHotfixSuffixHandler {

  /**
   * 处理哪种文件的后缀
   */
  val typeSuffix: String

  /**
   * 加载此文件
   *
   * 如果该文件已经被加载了，会先调用 [onFixUnload] 进行卸载，所以只会在成功卸载后才会调用这个方法
   *
   * 该方法允许你抛出 [ClassNotFoundException] 异常，在调用处会统一收集异常
   *
   * @param file File 为 [HotfixKotlinPlugin.hotfixRootFile] 下具体的某个文件，因为在加载前会先移动到 .run 文件夹下，如果加载失败，会移动回去
   */
  @Throws(ClassNotFoundException::class)
  suspend fun CommandSender.onFixLoad(file: File, pluginClassLoader: ClassLoader)

  /**
   * 卸载此文件
   *
   * 在这里你应该做以下几点：
   * - 取消协程和线程
   * - 取消所有类对该文件的引用
   *
   * @param file File 为 [HotfixKotlinPlugin.hotfixRootFile] 下具体的某个文件，因为在加载前会先移动到 .run 文件夹下
   * @return 返回 true，表示能够卸载；返回 false，表示不允许卸载
   */
  suspend fun CommandSender.onFixUnload(file: File): Boolean
}
