plugins {
  kotlin("jvm") version "1.7.10"
  id("com.gradle.plugin-publish") version "1.0.0-rc-3" // https://plugins.gradle.org/docs/publish-plugin
}

group = "io.github.985892345"
version = "1.3"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
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