package com.ndhzs

import com.ndhzs.hotfix.suffix.jar.JarEntrance
import com.ndhzs.hotfix.suffix.jar.JarHotfixUser
import com.ndhzs.hotfix.suffix.jar.getEntrance
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

/**
 * 一个测试热修的命令
 *
 * 实现 [JarHotfixUser] 接口即可与热修文件进行通信
 */
object DemoCommand : SimpleCommand(
  DemoPlugin, "test"
), JarHotfixUser {

  // 采用这种方式，防止一直持有引用而导致无法被卸载
  private val connect: IConnect?
    get() = getEntrance(IConnect::class.java)

  @Handler
  suspend fun CommandSender.test() {
    sendMessage("结果为：${connect?.get()}")
  }

  // 在热修将被卸载时回调，需要在这里清除掉所有对 IConnect 的引用并返回 true
  override fun onRemoveEntrance(entrance: JarEntrance): Boolean {
    return true
  }
}