package com.ndhzs

import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription

/**
 * 通过 SourceSet 实现热修的 demo
 *
 * 需要实现 [HotfixKotlinPlugin] 类
 */
object DemoSourceSet : HotfixKotlinPlugin(
  JvmPluginDescription(
    id = "com.ndhzs.demo-sourceSet",
    name = "通过 SourceSet 实现热修的 demo",
    version = "1.0",
  ) {
    author("985892345")
  },
  hotfixDirName = "demo", // 自定义热修的部署文件，部署文件在 /控制台目录/hotfix/demo 下
  hotfixCommandName = "demo" // 自定义热修命令，设置后热修名字为：fixdemo
) {
  override fun onHotfixDisable() {
    super.onHotfixDisable()
    HotfixTestCommand.register()
  }

  override fun onHotfixEnable() {
    super.onHotfixEnable()
    HotfixTestCommand.unregister()
  }
}