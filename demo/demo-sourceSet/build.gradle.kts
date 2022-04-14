plugins {
  val kotlinVersion = "1.6.20"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.serialization") version kotlinVersion

  id("net.mamoe.mirai-console") version "2.10.1"
}

group = "com.ndhzs"
version = "1.0"

repositories {
  maven("https://maven.aliyun.com/repository/public")
  mavenCentral()
  maven("https://jitpack.io")
}

/////////////////////////////////////////////////////////////
//
//       下面为配置 sourceSets，实现打热修包的模板写法
//
////////////////////////////////////////////////////////////

// 这里给全部源集设置依赖，下面还有一个用于给自定义源集设置依赖
dependencies {
  // 下面是用于本地测试时依赖本地的 jar 文件，测试使用
//  fun File.child(name: String): File {
//    return File(this, name)
//  }
//  // 注意：这个 implementation 本来并不会给你自己设置的源集添加依赖，
//  // 它只会给 main 源集添加依赖，但我后面让自定义源集依赖上了 main 的 compileClasspath，所以这里设置会给全部都设置
//  implementation(
//    fileTree(
//      rootDir.parentFile.parentFile.child("build").child("libs").toPath()
//    )
//  )

  implementation("com.ndhzs.mirai.hotfix:0.1")
}

// 需要新增的 sourceSets 的文件名
val hotfix = arrayOf(
  "hotfix-demo"
)
// 构建后，你会在 demo2/src 下看到 hotfix-demo 文件夹，且与 main 有相同的标识

// 这里会专门生成打热修代码的 sourceSets 文件夹，里面写需要热修的代码
sourceSets {
  hotfix.forEach {
    create(it) {
      // 依赖 main 的 output
      compileClasspath += sourceSets.named("main").get().output
      // 依赖 main 的 compileClasspath
      compileClasspath += sourceSets.named("main").get().compileClasspath
      java.srcDirs.forEach { file ->
        file.mkdirs()
        File(file.parentFile, "kotlin").mkdirs()
      }
      resources.srcDirs.forEach { file -> file.mkdirs() }
    }
  }
}

// 给 gradle 新增打热修包的任务，
// 位置在 idea  gradle 侧边栏 Tasks/hotfix/hotfix-demo 中（注意：这是一个单独的项目，请用 idea 单独打开才能看到）
//
hotfix.forEach {
  tasks.register<Jar>(it) {
    group = "hotfix"
    exclude("META-INF/**")
    archiveFileName.set("$it.jar")
    from(sourceSets.named(it).get().output)
    // 增加 runtimeClasspath
    from(sourceSets.named(it).get().runtimeClasspath.filter { file ->
      // 去掉与 main 中相同的 runtimeClasspath
      !sourceSets.named("main").get().runtimeClasspath.contains(file)
    }.map { file -> zipTree(file) })
  }
}

dependencies {
  // 使用这种方式单独给源集设置依赖，必须以上面设置的 hotfix-demo 开头
  "hotfix-demoImplementation"("com.google.code.gson:gson:2.9.0")
}
