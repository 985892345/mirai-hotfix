package com.ndhzs.hotfix

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.plugin.id
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
  suspend fun CommandSender.reload(@Name("正则") regex: String = ".+") {
    sendResultMessage(
      "reload:",
      plugin.controller.run { reloadHotfix(plugin, Regex(regex)) }
    ) {
      first.name + "\t   " + (second?.message ?: "success")
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
      "delete:",
      plugin.controller.run { deleteHotfix(plugin, Regex(regex)) }
    ) {
      first.name + "\t   " + (second?.message ?: "success")
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
      plugin.controller.run { cancelHotfix(plugin, Regex(regex)) }
    ) {
      first.name + "\t   " + (second?.message ?: "success")
    }
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
  suspend fun CommandSender.list(@Name("正则") regex: String = ".+") {
    sendResultMessage(
      "list:",
      plugin.controller.run { listHotfix(plugin, Regex(regex)) }
    ) {
      name
    }
  }
  
  /**
   * 发送文件进行热加载（只支持群文件），默认会撤回文件
   *
   * 命令 /fix... chat
   */
  @Description("发送文件进行热加载(只支持群文件)")
  @SubCommand
  suspend fun CommandSender.chat(@Name("是否撤回文件") isWithdrawn: Boolean = true) {
    plugin.controller.apply {
      chatHotfix(plugin, isWithdrawn) {
        sendResultMessage("chat reload:", it) {
          first.name + "\t   " + (second?.message ?: "success")
        }
      }
    }
  }
  
  suspend fun <T> CommandSender.sendResultMessage(
    entryWord: String,
    result: List<T>,
    description: T.(index: Int) -> String
  ) {
    if (result.isEmpty()) {
      sendMessage("${entryWord}\n未找到热修文件")
    } else {
      val joiner = StringJoiner("\n")
      joiner.add(entryWord)
      result.forEachIndexed { index, t ->
        joiner.add("${index + 1}、${description.invoke(t, index)}")
      }
      sendMessage(joiner.toString())
    }
  }
  
  init {
    // 初始化时加载上一次加载了的热修包
    ConsoleCommandSender.apply {
      plugin.launch {
        sendMessage(
          plugin.id + ":\n" +
            "默认热修加载文件地址：${plugin.controller.fileController.notLoadedDir}\n" +
            "默认热修运行文件地址: ${plugin.controller.fileController.loadedDir}"
        )
        val result = plugin.controller.run {
          reloadHotfix(
            plugin,
            Regex(".+"),
            plugin.controller.fileController.loadedDir
          )
        }
        sendResultMessage("init:", result) {
          first.name + "\t   " + (second?.message ?: "success")
        }
      }
    }
  }
}