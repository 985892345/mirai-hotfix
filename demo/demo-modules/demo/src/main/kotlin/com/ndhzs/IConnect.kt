package com.ndhzs

import com.ndhzs.hotfix.handler.suffix.jar.JarEntrance

/**
 * 定义用于通信的接口
 */
interface IConnect : JarEntrance {
  fun get(): String
}