@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://jitpack.io")
    mavenCentral()
  }
}

rootProject.name = "mirai-hotfix"
include("hotfix-base")
includeBuild("gradle-plugin")
include("hotfix-demo")
