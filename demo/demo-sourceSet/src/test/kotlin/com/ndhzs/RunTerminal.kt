package com.ndhzs

import java.io.File
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

fun setupWorkingDir() {
  // see: net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal
  System.setProperty("user.dir", File("debug-sandbox").absolutePath)
}

@ConsoleExperimentalApi
suspend fun main() {
  setupWorkingDir()

  MiraiConsoleTerminalLoader.startAsDaemon()

  val pluginInstance = DemoSourceSet

  pluginInstance.load()
  pluginInstance.enable()

  MiraiConsole.addBot(
    Bot_username,
    Bot_password
  ) {
    loadDeviceInfoJson(Bot_device)
  }.alsoLogin() // 登录一个测试环境的 Bot

  MiraiConsole.job.join()
}