plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("net.mamoe.mirai-console")
  id("io.github.985892345.mirai-hotfix") // 这里我直接用的模块内的插件，你需要单独写上版本号
}

configurations.all {
  resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

// 这里给全部源集设置依赖
dependencies {
  // 使用 gradle 插件后已经自动依赖了 mirai-hotfix
  shadowLink("io.github.985892345:mirai-hotfix")
}

hotfix {
  // 这里用于设置自定义源集并引入该源集单独使用的依赖，且会生成打包该源集的命令 gradlew hotfix-connect
  createHotfix("connect") {
    implementation("com.google.code.gson:gson:2.10")
  }
}


/*
* 1、connect 源集中主要写逻辑实现代码，main 源集中定义插件命令
*
* 2、新建的 connect 源集会自动依赖 main 源集，即可以实现在热修后调用 main 源集中的代码
*
* 3、调用 gradlew connect 即可进行打包成 jar，文件在 /build/libs 下
* */

