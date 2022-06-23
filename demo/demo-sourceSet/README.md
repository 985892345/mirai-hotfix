# demo-sourceSet
 本 demo 采用新建 `sourceSet` 的方式来进行依赖，相比采用子模块依赖更简便，也能与 `Mirai` 插件更好的配合

 **注意:** 该例子是一个单独的项目，需要使用 idea 单独打开

## 使用方式

### 1、引入依赖和插件
````kotlin
plugins {
  // 版本号在这里查看：https://plugins.gradle.org/plugin/io.github.985892345.mirai-hotfix
  id("io.github.985892345.mirai-hotfix") version "1.0"
}

// 插件中已经自动依赖了 mirai-hotfix
````

### 2、继承 HotfixKotlinPlugin
 在你需要被打热修的插件中改为继承 `HotfixKotlinPlugin`，该父类集成了打热修的命令

 具体可看：[DemoSourceSet](src/main/kotlin/com/ndhzs/DemoSourceSet.kt)

### 3、新增 SourceSet
```kotlin
hotfix {
  // 新建了一个 hotfix-demo 源集，且会自动生成打包该源集的命令 gradlew hotfix-demo
  createHotfix("hotfix-demo") {
    // 这里面写该源集单独使用的依赖
    implementation("com.google.code.gson:gson:2.9.0")
  }

  // 可以多写几个，但源集之间并不会自动依赖
}
```
注意事项：
- 新建的源集会自动依赖 main
- 多个新增的源集并不会自动依赖

 可以参考: [build.gradle.kts](build.gradle.kts)

### 4、新建用于交互的接口
 在 main 源集中新建一个接口来进行通信，比如例子中的 [IConnect](src/main/kotlin/com/ndhzs/IConnect.kt)，
 然后在上面建好的源集中实现该接口

### 5、部署热修文件
- 打包用于热修的源集，打包命令已自动生成
- 部署到 `/控制台/hotfix/demo/` 目录下
- 执行命令：`fixdemo reload [keyword]`
- 机器人返回加载成功，则热修成功，热修文件会被剪切到 `.run` 文件夹下

> 1、如何打包可以参考模板：[build.gradle.kts](build.gradle.kts)   
> 2、除了 `reload` 命令以外，还有 `remove`、`list` 命令  
> 3、推荐使用一些 idea 插件进行一键部署，比如：Alibaba Cloud Toolkit  
> 4、如果你的项目名称为 demo，则热修命令为 fixdemo，该命令可以自定义，自定义请看 `HotfixKotlinPlugin` 类


## 其他示例
- [mirai-younger-study](https://github.com/985892345/mirai-younger-study): 用来提醒同学交青年大学习截图的 `miria` 插件

 
