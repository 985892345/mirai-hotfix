plugins {
  val kotlinVersion = "1.7.10"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.serialization") version kotlinVersion

  id("net.mamoe.mirai-console") version "2.12.1"
  id("io.github.985892345.mirai-hotfix") version "1.3"// 引入该插件
}

group = "io.github.985892345"
version = "1.0"

repositories {
  maven("https://maven.aliyun.com/repository/public")
  mavenCentral()
}

// 这里给全部源集设置依赖
dependencies {
  // 使用 gradle 插件后已经自动依赖了 mirai-hotfix
}

hotfix {
  // 这里用于设置自定义源集并引入该源集单独使用的依赖，且会生成打包该源集的命令 gradlew hotfix-demo
  createHotfix("hotfixDemo") {
    implementation("com.google.code.gson:gson:2.9.0")
  }
  
  // 可以多写几个，但源集之间并不会自动依赖
}


/*
* 1、hotfix-demo 源集中主要写逻辑实现代码，main 源集中定义插件命令
*
* 2、新建的 hotfix-demo 源集会自动依赖 main 源集，即可以实现在热修后调用 main 源集中的代码
*
* 3、调用 gradlew hotfix-demo 即可进行打包成 jar，文件在 /build/libs 下
* */