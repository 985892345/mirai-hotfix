import java.util.Properties

plugins {
  kotlin("jvm") version "1.7.20"
  id("com.gradle.plugin-publish") version "1.0.0-rc-3" // https://plugins.gradle.org/docs/publish-plugin
}

val properties = Properties().apply {
  load(rootDir.parentFile.resolve("version.properties").inputStream())
}

group = properties.getValue("group")
if (gradle.parent!!.startParameter.taskNames.contains(":gradle-plugin:publishPlugins")) {
  val hotfixVersion = properties.getValue("version").toString()
  val oldVersionFile = rootDir.resolve("oldVersion.txt")
  // 因为 gradle 不允许发布 SNAPSHOT 结尾的插件，所以只能装换为 alpha 版本
  version = if (hotfixVersion.endsWith("SNAPSHOT")) {
    val oldVersion = oldVersionFile.readText()
    val oldVersionNum = Regex("[0-9]+.[0-9]+.[0-9]").find(oldVersion)?.value
    val nowVersionNum = Regex("[0-9]+.[0-9]+.[0-9]").find(hotfixVersion)?.value
    if (oldVersionNum != nowVersionNum) {
      hotfixVersion.replace("SNAPSHOT", "alpha1")
    } else {
      val alphaVersionNum = Regex("(?<=alpha)[1-9][0-9]*").find(oldVersion)?.value
      if (alphaVersionNum == null) {
        hotfixVersion.replace("SNAPSHOT", "alpha1")
      } else {
        val newAlphaVersionNum = (alphaVersionNum.toInt() + 1).toString()
        hotfixVersion.replace("SNAPSHOT", "alpha${newAlphaVersionNum}")
      }
    }
  } else hotfixVersion
  oldVersionFile.writeText(
    "// 用于记录上一次发布时的版本号，因为 gradle 不允许发布 SNAPSHOT 结尾的插件，所以只能装换为 alpha 版本，然后每次发布自动加 1\n"
      + version.toString()
  )
  
  // 写入 mirai-hotfix 的版本号
  project.rootDir
    .resolve("src")
    .resolve("main")
    .resolve("java")
    .resolve("HotfixVersion.kt")
    .apply {
      createNewFile()
      writeText("internal val `hotfix-version` = \"${hotfixVersion}\"")
    }
}

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
