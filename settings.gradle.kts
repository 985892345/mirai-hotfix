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
    // mavenCentral 快照仓库
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
  }
}

rootProject.name = "mirai-hotfix"
includeBuild("gradle-plugin")
include("hotfix-demo")
include("hotfix-base")
