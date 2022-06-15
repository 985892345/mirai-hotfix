# demo-sourceSet
 本 demo 采用新建 `sourceSet` 的方式来进行依赖，相比采用子模块依赖更简便，也能与 `Mirai` 插件更好的配合

 **注意:** 该例子是一个单独的项目，需要使用 idea 单独打开

## 使用方式

### 1、引入依赖
[![](https://jitpack.io/v/985892345/mirai-hotfix.svg)](https://jitpack.io/#985892345/mirai-hotfix)
````kotlin
repositories {
  // 引入 jitpack 仓库地址
  maven("https://jitpack.io")
}

dependencies {
  implementation("com.github.985892345:mirai-hotfix:xxx")
}
````

### 2、继承 HotfixKotlinPlugin
 在你需要被打热修的插件中改为继承 `HotfixKotlinPlugin`，该父类集成了打热修的命令

 具体可看：[DemoSourceSet](src/main/kotlin/com/ndhzs/DemoSourceSet.kt)

### 3、修改 build.gradle
 需要修改 `build.gradle` 来新增 `sourceSet` 文件夹

 这里提供一个模板: [build.gradle.kts](build.gradle.kts)

### 4、新建用于交互的接口
 在 main 源集中新建一个接口来进行通信，比如例子中的 [IConnect](src/main/kotlin/com/ndhzs/IConnect.kt)，
 然后在上面建好的源集中实现该接口

### 5、部署热修文件
- 打包用于热修的 `sourceSet`
- 部署到 `/控制台/hotfix/demo/` 目录下
- 执行命令：`fixdemo reload [keyword]`
- 机器人返回加载成功，则热修成功，热修文件会被剪切到 `.run` 文件夹下

> 1、如何打包可以参考模板：[build.gradle.kts](build.gradle.kts)   
> 2、除了 `reload` 命令以外，还有 `remove`、`list` 命令  
> 3、推荐使用一些 idea 插件进行一键部署，比如：Alibaba Cloud Toolkit  
> 4、如果你的项目名称为 demo，则热修命令为 fixdemo，该命令可以自定义，自定义请看 `HotfixKotlinPlugin` 类

 
