package com.ndhzs

import com.ndhzs.hotfix.handler.suffix.jar.JarEntrance

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/13 13:05
 */
interface IConnect : JarEntrance {
  fun get(): String
}