import java.util.Properties

plugins {
  kotlin("jvm") version "1.7.10"
  id("com.gradle.plugin-publish") version "1.0.0-rc-3" // https://plugins.gradle.org/docs/publish-plugin
}

val properties = Properties().apply {
  load(rootDir.parentFile.resolve("version.properties").inputStream())
}

group = properties.getValue("group")
version = properties.getValue("version")

repositories {
  maven("https://maven.aliyun.com/repository/public")
  mavenCentral()
}

gradlePlugin {
  plugins {
    create("miraiSourceSetHotfix") {
      id = "io.github.985892345.mirai-hotfix"
      displayName = "基于 mirai-console 的热修插件"
      description = "本插件自动生成 mirai-hotfix 所需要的 SourceSet 文件"
      implementationClass = "com.ndhzs.HotfixPlugin"
    }
  }
}

pluginBundle {
  website = "https://github.com/985892345/mirai-hotfix"
  vcsUrl = "https://github.com/985892345/mirai-hotfix"
  tags = listOf("kotlin", "mirai", "hotfix")
}

val buildConfigFile = project.rootDir
  .resolve("src")
  .resolve("main")
  .resolve("java")
  .resolve("BuildConfig.kt")

buildConfigFile.delete()
buildConfigFile.createNewFile()

buildConfigFile.writeText("internal val `mirai-hotfix-version` = \"$version\"")
