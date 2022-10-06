package com.ndhzs.hotfix.controller

import net.mamoe.mirai.console.command.CommandSender
import java.io.File

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @date 2022/10/6 11:10
 */
interface IHotfixController {

  /**
   * 加载热修
   */
  fun reloadHotfix(sender: CommandSender, regex: Regex): List<Pair<File, Throwable?>>

  /**
   * 加载热修
   * @param dir 指定文件夹
   */
  fun reloadHotfix(sender: CommandSender, regex: Regex, dir: File): List<Pair<File, Throwable?>>

  /**
   * 删除热修
   */
  fun deleteHotfix(sender: CommandSender, regex: Regex): List<Pair<File, Throwable?>>

  /**
   * 取消热修，不会删除，但会把它移到存放未加载文件的文件夹中
   */
  fun cancelHotfix(sender: CommandSender, regex: Regex): List<Pair<File, Throwable?>>

  /**
   * 取消热修
   * @param dir 取消后的文件移到指定文件夹中
   */
  fun cancelHotfix(sender: CommandSender, regex: Regex, dir: File): List<Pair<File, Throwable?>>

  /**
   * 列出热修
   */
  fun listHotfix(sender: CommandSender, regex: Regex): List<File>
}