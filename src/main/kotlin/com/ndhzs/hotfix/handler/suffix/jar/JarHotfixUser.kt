package com.ndhzs.hotfix.handler.suffix.jar

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/11 11:19
 */
interface JarHotfixUser {
  /**
   * 在卸载时回调
   *
   * 如果允许卸载，请在这里消掉所有对 [entrance] 的引用，不然卸载会失败
   *
   * @param entrance 被卸载的启动类
   * @return 返回 true，则允许卸载；返回 false，则不允许卸载
   */
  fun onRemoveEntrance(entrance: JarEntrance): Boolean
}

/**
 * 通过 [clazz] 得到你需要的热修文件
 *
 * 不建议使用一个变量来接受返回值，因为这样可能会导致卸载的不便，
 * 更建议采用下面这种写法
 * ```
 * private val connect: IConnect?
 *   get() = getEntrance(IConnect::class.java)
 *
 * fun test() {
 *   println(connect?.get())
 * }
 * ```
 */
@Suppress("UNCHECKED_CAST")
fun <T: JarEntrance> JarHotfixUser.getEntrance(clazz: Class<T>): T? {
  JarHotfixSuffixHandler.jarByFileName.forEach {
    if (clazz.isAssignableFrom(it.value.entrance.javaClass)) {
      return it.value.entrance as T
    }
  }
  return null
}