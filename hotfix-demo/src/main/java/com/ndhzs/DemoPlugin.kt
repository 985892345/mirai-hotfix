package com.ndhzs

import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/9/6 16:41
 */
object DemoPlugin : HotfixKotlinPlugin(
  JvmPluginDescription(
    id = "com.ndhzs.demo-sourceSet",
    name = "通过 SourceSet 实现热修的 demo",
    version = "1.0",
  ) {
    author("985892345")
  },
  hotfixCommandName = "demo" // 自定义热修命令，设置后热修名字为：fixdemo
) {
  override fun onHotfixEnable() {
    super.onHotfixEnable()
    DemoCommand.register()
  }
  
  override fun onHotfixDisable() {
    super.onHotfixDisable()
    DemoCommand.unregister()
  }
}