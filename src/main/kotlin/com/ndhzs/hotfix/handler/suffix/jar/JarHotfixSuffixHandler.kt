package com.ndhzs.hotfix.handler.suffix.jar

import com.ndhzs.hotfix.handler.suffix.IHotfixSuffixHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
object JarHotfixSuffixHandler : IHotfixSuffixHandler {

  override val typeSuffix = "jar"

  internal val jarByFileName = mutableMapOf<String, Jar>()

  override suspend fun CommandSender.onFixLoad(file: File, pluginClassLoader: ClassLoader) {
    val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()), pluginClassLoader)
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
            entrance.apply { onFixLoad() }
            jarByFileName[file.name] = Jar(file, entrance, classLoader, mutableListOf())
          }
        }
      }
    }
  }

  override suspend fun CommandSender.onFixUnload(file: File): Boolean {
    jarByFileName[file.name]?.apply { unload() }
    jarByFileName.remove(file.name)
    return true
  }

  class Jar(
    val file: File,
    val entrance: JarEntrance,
    val classLoader: URLClassLoader,
    val hotfixUsers: MutableList<JarHotfixUser>
  ) {
    /**
     * 删除 jar
     *
     * **NOTE:** 这里只是移去了引用，但必须要调用 System.gc() 用于彻底删除引用，这样本地文件才可以被覆盖
     */
    suspend fun CommandSender.unload(): Boolean {
      if (hotfixUsers.all { it.onRemoveEntrance(entrance) }) {
        entrance.apply { onFixUnload() }
        classLoader.close() // 关闭对资源的读取
        return true
      }
      return false
    }
  }
}