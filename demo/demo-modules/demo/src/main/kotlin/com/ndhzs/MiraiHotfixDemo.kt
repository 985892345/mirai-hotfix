package com.ndhzs

import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.handler.suffix.jar.JarEntrance
import com.ndhzs.hotfix.handler.suffix.jar.JarHotfixUser
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription

object MiraiHotfixDemo : HotfixKotlinPlugin(
  JvmPluginDescription(
    id = "com.ndhzs.hotfix-plugin-demo",
    name = "热修的demo",
    version = "1.0",
  ) {
    author("985892345")
  },
  hotfixDirName = "demo",
  hotfixCommandName = "demo"
) {

  override fun onHotfixEnable() {
    super.onHotfixEnable()
    HotfixTestCommand.register()
  }

  override fun onHotfixDisable() {
    super.onHotfixDisable()
    HotfixTestCommand.unregister()
  }
}
