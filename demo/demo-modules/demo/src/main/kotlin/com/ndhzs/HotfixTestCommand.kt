package com.ndhzs

import com.ndhzs.hotfix.handler.suffix.jar.JarEntrance
import com.ndhzs.hotfix.handler.suffix.jar.JarHotfixUser
import com.ndhzs.hotfix.handler.suffix.jar.getEntrance
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/11 14:25
 */
object HotfixTestCommand : SimpleCommand(
  MiraiHotfixDemo, "test"
), JarHotfixUser {

  private val connect: IConnect?
    get() = getEntrance(IConnect::class.java)

  @Handler
  suspend fun CommandSender.test() {
    sendMessage("结果为：${connect?.get()}")
  }

  override fun onRemoveEntrance(entrance: JarEntrance): Boolean {
    return true
  }
}