package com.ndhzs.hotfix

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import java.util.StringJoiner

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 14:48
 */
@OptIn(ConsoleExperimentalApi::class)
class HotfixCommand(
  private val plugin: HotfixKotlinPlugin
) : CompositeCommand(
  plugin,
  "fix${plugin.hotfixCommandName}",
  description = "${plugin.hotfixCommandName}的热修指令"
) {

  /**
   * 重新加载某个热修包，会先删除旧的热修包（如果有的话）再加载新的热修包
   *
   * 命令 /fix... reload regex
   *
   * @param regex 正则
   */
  @Description("重新加载某个热修包")
  @SubCommand
  suspend fun CommandSender.reload(@Name("正则") regex: String) {
    sendResultMessage(
      "reload:",
      plugin.controller.reloadHotfix(this, Regex(regex))
    ) {
      first.name + "\t" + (second?.message ?: "加载成功")
    }
  }

  /**
   * 删除某个热修包
   *
   * 命令 /fix... remove regex
   *
   * @param regex 正则
   */
  @Description("删除某个热修包")
  @SubCommand
  suspend fun CommandSender.delete(@Name("正则") regex: String) {
    sendResultMessage(
      "delete",
      plugin.controller.deleteHotfix(this, Regex(regex))
    ) {
      first.name + "\t" + (second?.message ?: "删除成功")
    }
  }

  /**
   * 取消某个热修包，卸载但不删除，卸载成功后会移动到未加载的文件夹中
   *
   * 命令 /fix... cancel regex
   *
   * @param regex 正则
   */
  @Description("取消某个热修包")
  @SubCommand
  suspend fun CommandSender.cancel(@Name("正则") regex: String) {
    sendResultMessage(
      "cancel:",
      plugin.controller.cancelHotfix(this, Regex(regex))
    ) {
      first.name + "\t" + (second?.message ?: "取消成功")
    }
  }

  /**
   * 列出全部的热修包
   *
   * 命令 /fix... list
   */
  @Description("查看加载了哪些热修包")
  @SubCommand
  suspend fun CommandSender.list() {
    list(".+")
  }

  /**
   * 查看加载了哪些热修包
   *
   * 命令 /fix... list regex
   *
   * @param regex 正则
   */
  @Description("查看加载了哪些热修包")
  @SubCommand
  suspend fun CommandSender.list(@Name("正则") regex: String) {
    sendResultMessage(
      "list:",
      plugin.controller.listHotfix(this, Regex(regex))
    ) {
      name
    }
  }

  private suspend fun <T> CommandSender.sendResultMessage(
    entryWord: String,
    result: List<T>,
    description: T.(index: Int) -> String
  ) {
    val joiner = StringJoiner("\n")
    joiner.add(entryWord)
    result.forEachIndexed { index, t ->
      joiner.add("${index + 1}、${description.invoke(t, index)}")
    }
    if (result.isEmpty()) {
      sendMessage("未找到该文件")
    } else {
      sendMessage(joiner.toString())
    }
  }

  init {
    // 初始化时加载上一次加载了的热修包
    val result = plugin.controller
      .reloadHotfix(
        ConsoleCommandSender,
        Regex(".+"),
        plugin.controller.fileController.loadedDir
      )
    ConsoleCommandSender.launch {
      ConsoleCommandSender.sendResultMessage("init:", result) {
        first.name + "\t" + (second?.message ?: "success")
      }
    }
  }
}