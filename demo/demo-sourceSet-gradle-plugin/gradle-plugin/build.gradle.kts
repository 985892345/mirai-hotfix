plugins {
  kotlin("jvm") version "1.6.21"
  `java-gradle-plugin`
  `maven-publish`
  id("com.gradle.plugin-publish") version "1.0.0-rc-3"
}

group = "io.github.985892345"
version = "1.0-alpha6"

repositories {
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

publishing {
  repositories {
    maven {
      name = "localTest"
      url = uri("./build")
    }
  }
}