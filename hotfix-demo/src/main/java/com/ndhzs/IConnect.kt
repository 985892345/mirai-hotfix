package com.ndhzs

import com.ndhzs.hotfix.handler.suffix.jar.JarEntrance

/**
 * 定义接口用于分离逻辑代码
 *
 * 需要实现 [JarEntrance] 接口
 *
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/4/13 13:05
 */
interface IConnect : JarEntrance {
  fun get(): String
}