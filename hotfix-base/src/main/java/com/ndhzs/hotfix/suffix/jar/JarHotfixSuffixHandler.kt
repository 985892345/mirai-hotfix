package com.ndhzs.hotfix.suffix.jar

import com.ndhzs.hotfix.comand.HotfixCommandSender
import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.suffix.AbstractHotfixSuffixHandler
import kotlinx.coroutines.*
import net.mamoe.mirai.console.command.CommandSender
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

/**
 * Jar 文件的热修处理者
 *
 * 如果要使用热修文件的话，请实现 [JarHotfixUser] 接口
 *
 * **NOTE：** 为了快速读取，启动类规定只能放在 jar 根路径下
 */
object JarHotfixSuffixHandler : AbstractHotfixSuffixHandler("jar") {

  internal val jarByFileName = mutableMapOf<String, Jar>()

  override suspend fun CommandSender.onFixLoad(plugin: HotfixKotlinPlugin, file: File) {
    val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()), plugin.javaClass.classLoader)
    val jarFile = withContext(Dispatchers.IO) { JarFile(file) }
    val entries = jarFile.entries()
    while (entries.hasMoreElements()) {
      val element = entries.nextElement()
      // 只允许启动类在根目录下
      if (!element.name.contains("/")) {
        val className = element.name
        // 排除掉 Kt.class 结尾、包含 $ 符号的类
        if (className.endsWith(".class")
          && !className.endsWith("Kt.class")
          && !className.contains("$")
        ) {
          val clazz = classLoader.loadClass(className.substringBeforeLast("."))
          if (JarEntrance::class.java.isAssignableFrom(clazz)) {
            val entrance = clazz.getDeclaredConstructor().newInstance() as JarEntrance
            val supervisorJob = plugin.launch {
            
            }
            entrance.apply { onFixLoad(plugin) }
            jarByFileName[file.name] = Jar(file, entrance, classLoader, mutableListOf())
          }
        }
      }
    }
  }

  override suspend fun CommandSender.onFixUnload(plugin: HotfixKotlinPlugin, file: File): Boolean {
    jarByFileName[file.name]?.apply { unload(plugin, this@onFixUnload) }
    jarByFileName.remove(file.name)
    return true
  }
  
  override suspend fun onEnable(plugin: HotfixKotlinPlugin, file: File?) {
    if (file == null) {
      jarByFileName.forEach {
        it.value.entrance.onEnable(plugin)
      }
    } else {
      jarByFileName[file.name]?.entrance?.onEnable(plugin)
    }
  }
  
  override suspend fun onDisable(plugin: HotfixKotlinPlugin, file: File?) {
    if (file == null) {
      jarByFileName.forEach {
        it.value.entrance.onDisable(plugin)
      }
    } else {
      jarByFileName[file.name]?.entrance?.onDisable(plugin)
    }
  }
  
  
  class Jar(
    val file: File,
    val entrance: JarEntrance,
    val classLoader: URLClassLoader,
    val hotfixUsers: MutableList<JarHotfixUser>,
    val supervisorJob: Job
  ) {
    /**
     * 删除 jar
     *
     * **NOTE:** 这里只是移去了引用，但必须要调用 System.gc() 用于彻底删除引用，这样本地文件才可以被覆盖
     */
    suspend fun unload(plugin: HotfixKotlinPlugin, sender: HotfixCommandSender): Boolean {
      if (hotfixUsers.all { it.onRemoveEntrance(entrance) }) {
        entrance.apply { sender.onFixUnload(plugin) }
        withContext(Dispatchers.IO) {
          classLoader.close() // 关闭对资源的读取
        }
        return true
      }
      return false
    }
  }
}