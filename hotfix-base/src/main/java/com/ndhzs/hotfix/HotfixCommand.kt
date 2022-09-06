package com.ndhzs.hotfix

import com.ndhzs.hotfix.handler.load.HotfixReload
import com.ndhzs.hotfix.handler.load.HotfixRemove
import com.ndhzs.hotfix.handler.suffix.IHotfixSuffixHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.rootDir
import java.io.File

/**
 * 热修命令
 *
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/11 11:13
 */
internal class HotfixCommand(
  private val hotfixPlugin: HotfixKotlinPlugin,
  hotfixDirName: String,
  hotfixCommandName: String,
  typeHandlers: List<IHotfixSuffixHandler>
) : CompositeCommand(
  hotfixPlugin, "fix${hotfixCommandName}",
  description = "${hotfixCommandName}的热修指令",
) {

  val hotfixRootFile = MiraiConsole.rootDir.resolve("hotfix").apply { mkdirs() }
  val hotfixLoadFile = File(hotfixRootFile, hotfixDirName).apply { mkdirs() }
  val hotfixRunningFile = File(hotfixLoadFile, ".run").apply { mkdirs() }

  val hotfixTypeHandlerBySuffix = mutableMapOf<String, IHotfixSuffixHandler>()
  val runningHotfixByFileName = mutableMapOf<String, HotfixKotlinPlugin.HotfixFile>()
  private val logger = hotfixPlugin.logger

  init {
    typeHandlers.forEach {
      val typeHandler = hotfixTypeHandlerBySuffix[it.typeSuffix]
      if (typeHandler != null) {
        logger.warning("${it.typeSuffix} 后缀已存在处理者：${typeHandler::class.simpleName}，${it::class.simpleName} 添加失败")
      } else {
        hotfixTypeHandlerBySuffix[it.typeSuffix] = it
      }
    }
    hotfixPlugin.launch {
      ConsoleCommandSender.apply {
        reloadFile(this, emptyArray(), hotfixRunningFile).apply {
          if (this != null) {
            logger.info("\n${hotfixPlugin.description.id} 初始化加载文件如下：\n${weaveReloadResult(this)}")
          } else {
            logger.info("无初始化热修文件")
          }
        }
      }
    }
  }

  /**
   * 重新加载某个热修包，会先卸载旧的热修包（如果有的话）再加载新的热修包
   *
   * 命令 /fix... reload keyword1 keyword2 keyword3...
   *
   * @param keyword 文件名的关键字，当输入多个时会取并集
   */
  @Description("重新加载某个热修包")
  @SubCommand
  suspend fun CommandSender.reload(vararg keyword: String) {
    sendMessage(
      weaveReloadResult(
        reloadFile(this, keyword)
      )
    )
  }

  private suspend fun reloadFile(
    commandSender: CommandSender,
    keyword: Array<out String>,
    whereFile: File = hotfixLoadFile
  ): HotfixReload.ReloadState? {
    return commandSender.run {
      HotfixReload.run {
        reloadFiles(
          hotfixPlugin,
          findFile(whereFile, keyword),
          hotfixRunningFile, hotfixTypeHandlerBySuffix, runningHotfixByFileName
        )?.apply {
          reloadSuccessList.forEach {
            runningHotfixByFileName[it.file.name] = it
          }
        }
      }
    }
  }

  /**
   * 移除某个热修包
   *
   * 命令 /fix... remove keyword1 keyword2 keyword3...
   *
   * @param keyword 文件名的关键字，当输入多个时会取并集
   */
  @Description("移除某个热修包")
  @SubCommand
  suspend fun CommandSender.remove(vararg keyword: String) {
    sendMessage(
      weaveRemoveResult(
        removeFile(this, keyword)
      )
    )
  }

  private suspend fun removeFile(
    commandSender: CommandSender,
    keyword: Array<out String>,
  ): HotfixRemove.RemoveState? {
    return commandSender.run {
      HotfixRemove.run {
        removeFiles(findFile(hotfixRunningFile, keyword), runningHotfixByFileName)
      }
    }
  }

  /**
   * 查看加载了哪些热修包
   *
   * 命令 /fix... list keyword
   *
   * @param keyword 文件名的关键字，当输入多个时会取并集
   */
  @Description("查看加载了哪些热修包")
  @SubCommand
  suspend fun CommandSender.list(vararg keyword: String) {
    sendMessage(
      weaveListResult(keyword)
    )
  }

  private fun findFile(dirFile: File, must: Array<out String> = emptyArray()): Array<File> {
    return dirFile.listFiles { _, name ->
      must.all { name.contains(it) } && name != hotfixRunningFile.name
    } ?: emptyArray()
  }

  /**
   * 编织热修结果的数据为 String
   */
  private fun weaveReloadResult(
    reloadState: HotfixReload.ReloadState?
  ): String {
    if (reloadState != null) {
      return StringBuilder()
        .weave("加载成功", reloadState.reloadSuccessList.map { it.file }, false)
        .weave("无法处理", reloadState.noHandlerList)
        .weave("不能卸载", reloadState.removeState?.unloadFailureList?.map { it.file })
        .weave("无法卸载", reloadState.removeState?.closeFailureList?.map { it.file })
        .weave("删除失败", reloadState.removeState?.deleteFailureList?.map { it.file })
        .weave("读取失败", reloadState.readFailureList)
        .weave("移动失败", reloadState.moveFailureList)
        .toString()
    }
    return "未找到该文件！"
  }

  private fun weaveRemoveResult(
    removeState: HotfixRemove.RemoveState?
  ): String {
    if (removeState != null) {
      return StringBuilder()
        .weave("删除成功", removeState.removeSuccessList.map { it.file }, false)
        .weave("不能卸载", removeState.unloadFailureList.map { it.file })
        .weave("无法卸载", removeState.closeFailureList.map { it.file })
        .weave("删除失败", removeState.deleteFailureList.map { it.file })
        .toString()
    }
    return "未找到该文件！"
  }

  private fun weaveListResult(
    keyword: Array<out String>
  ): String {
    val findList = mutableListOf<File>()
    runningHotfixByFileName.forEach { entry ->
      if (keyword.all { entry.key.contains(it) }) {
        findList.add(entry.value.file)
      }
    }
    return StringBuilder()
      .weave("发现以下热修文件", findList, false)
      .toString()
  }

  private fun StringBuilder.weave(str: String, list: List<File>?, ignoreEmpty: Boolean = true): StringBuilder {
    if (list == null || ignoreEmpty && list.isEmpty()) {
      return this
    }
    append(str).append("：").appendLine(list.size)
    repeat(list.size) {
      append("${it + 1}、").append(list[it].name)
      if (it < list.size - 1) {
        append('\n')
      }
    }
    return this
  }
}