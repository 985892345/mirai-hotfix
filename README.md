# mirai-hotfix
![Maven Central](https://img.shields.io/maven-central/v/io.github.985892345/mirai-hotfix)


 基于 Mirai Console 实现的热修插件

 mirai 项目：https://github.com/mamoe/mirai
 
## 使用方式
 由于热修一般需要与其他代码进行交互，所以这里提供了两种使用方式：

### 1、使用源集形式依赖
适合简单的场景

该方法只需要使用一个 gradle 插件即可
```kotlin
plugins {
  id("io.github.985892345.mirai-hotfix") version "xxx" // 版本号请查看标签
}
```
为了配合 Mirai 官方插件生成的项目，我更推荐采用此方式，只需要在源代码的基础上修改少量代码即可接入热修

详细教程：[demo](hotfix-demo)

### 2、使用子模块依赖
适合复杂的场景

单独依赖热修基础包
```kotlin
dependencies {
  implementation("io.github.985892345:mirai-hotfix:xxx") // 版本号与上面一致
}
```

> 如果你有 Android 多模块开发经验，也许会比较轻松
> 这里不给出详细的使用方式


## 功能
- [x] 通过 jar 包进行热修
- [x] 将 jar 包丢在群里即可热修

 欢迎各位在 issues 中提出建议
 
## 实现思路
 使用接口来实现 定义命令的插件代码 与 逻辑实现代码 的分离，从而可以随时替换逻辑代码，
 在没有修改底层代码而实现简单的热修