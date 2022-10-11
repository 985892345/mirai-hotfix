package com.ndhzs.hotfix.controller

import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.contact.Member
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
  suspend fun CommandSender.reloadHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<Pair<File, Throwable?>>
  
  /**
   * 加载热修
   * @param dir 指定文件夹
   */
  suspend fun CommandSender.reloadHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex,
    dir: File
  ): List<Pair<File, Throwable?>>
  
  /**
   * 删除热修
   */
  suspend fun CommandSender.deleteHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<Pair<File, Throwable?>>
  
  /**
   * 取消热修，不会删除，但会把它移到存放未加载文件的文件夹中
   */
  suspend fun CommandSender.cancelHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<Pair<File, Throwable?>>
  
  /**
   * 取消热修
   * @param dir 取消后的文件移到指定文件夹中
   */
  suspend fun CommandSender.cancelHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex,
    dir: File
  ): List<Pair<File, Throwable?>>
  
  /**
   * 列出热修
   */
  suspend fun CommandSender.listHotfix(
    plugin: HotfixKotlinPlugin,
    regex: Regex
  ): List<File>
  
  /**
   * 聊天热修
   * @param isWithdrawn 是否撤回文件
   */
  suspend fun CommandSender.chatHotfix(
    plugin: HotfixKotlinPlugin,
    isWithdrawn: Boolean,
    result: suspend (List<Pair<File, Throwable?>>) -> Unit
  )
}