plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("net.mamoe.mirai-console")
}

version = "1.0"

/////////////////////////////////////////////////////////////
//
//       下面为配置热修模块，实现打热修包的模板写法
//
////////////////////////////////////////////////////////////

// 这里写被热修的模块
val hotfixModule = ":demo"

repositories {
  maven("https://maven.aliyun.com/repository/public")
  maven("https://jitpack.io") // 引入 jitpack
  mavenCentral()
}

// 创建一个直接打包 jar 的 task
tasks.register<Jar>(project.name) {
  group = "hotfix"
  exclude("META-INF/**")
  // 会在 build/libs 下生成 jar 包
  archiveFileName.set("${project.name}.jar")
  from(sourceSets.main.get().output)
  from(configurations.runtimeClasspath.get().filter {
    // 去掉与被热修模块的相同依赖
    !project(hotfixModule).configurations.runtimeClasspath.get().contains(it)
      && it.name.endsWith(".jar")
  }.map { zipTree(it) })
}

dependencies {
  // 依赖被热修的模块，注意使用 compileOnly
  compileOnly(project(hotfixModule))
}