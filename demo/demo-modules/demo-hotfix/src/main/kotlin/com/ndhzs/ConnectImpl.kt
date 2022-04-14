package com.ndhzs

import net.mamoe.mirai.console.command.CommandSender

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/11 12:10
 */
class ConnectImpl : IConnect {
  override fun get(): String {
//    return "123"
    return "abc"
  }

  override suspend fun CommandSender.onFixLoad() {
  }

  override suspend fun CommandSender.onFixUnload(): Boolean {
    return true
  }
}