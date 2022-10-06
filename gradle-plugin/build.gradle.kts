import java.util.Properties

plugins {
  kotlin("jvm") version "1.7.20"
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

tasks.register("createBuildConfig") {
  dependsOn("publishPlugins")
  doFirst {
    val versionOtherText = "internal val `mirai-hotfix-version` = "

    val buildConfigFile = project.rootDir
      .resolve("src")
      .resolve("main")
      .resolve("java")
      .resolve("BuildConfig.kt")

    if (!version.toString().endsWith("SNAPSHOT")) {
      if (buildConfigFile.exists()) {
        buildConfigFile.writer()
      } else {
        buildConfigFile.createNewFile()
        buildConfigFile.writeText("internal val `mirai-hotfix-version` = \"$version\"")
      }
    } else {

    }

    val text = buildConfigFile.readText()
    val oldVersion = Regex("(?<=$versionOtherText\")[^\"]+").find(text)?.value
    if (oldVersion != null) {
      val suffix = oldVersion.substringAfterLast("-")
      if (suffix != oldVersion) {
        var alphaVersion = Regex("(?<=alpha)[0-9]+").find(suffix)?.value
        if (alphaVersion == null) {
          alphaVersion = "1"
        } else {
          alphaVersion = (alphaVersion.toInt() + 1).toString()
        }

      } else {

      }
    }
  }
}

