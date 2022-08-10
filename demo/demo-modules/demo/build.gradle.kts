plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("net.mamoe.mirai-console")
}

group = "com.ndhzs"
version = "1.0"

repositories {
  maven("https://maven.aliyun.com/repository/public")
  maven("https://jitpack.io")
  mavenCentral()
}

dependencies {
  
  // 下面是用于本地测试时依赖本地的 jar 文件，测试使用
  //    fun File.child(name: String): File {
  //        return File(this, name)
  //    }
  //    // 这里需要使用 api，为了让热修的模块也能得到依赖
  //    api(
  //        fileTree(
  //            rootDir.parentFile.parentFile.child("build").child("libs").toPath()
  //        )
  //    )
  
  // 这里需要使用 api，为了让热修的模块也能得到依赖
  api("com.github.985892345:mirai-hotfix:1.1") // 该版本可能不是最新版
}