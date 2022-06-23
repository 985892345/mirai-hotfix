# demo-sourceSet-gradle-plugin
自定义源集使用的 gradle 插件，已发布至 gradle 官方插件仓库

## 使用方式
````kotlin
plugins {
  // 版本号在这里查看：https://plugins.gradle.org/plugin/io.github.985892345.mirai-hotfix
  id("io.github.985892345.mirai-hotfix") version "1.0"
}

// 插件中已经自动依赖了 mirai-hotfix
````

## 功能
- 自动生成 `SourceSet` 
- 自动生成打包上述源集的 task（默认命令就为源集名字）
- 自动依赖 `mirai-hotfix`
- 已添加 jitpack 地址

## 注意事项
该插件是为 新建 SourceSet 热修方案使用的