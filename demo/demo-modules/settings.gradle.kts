pluginManagement {
  repositories {
    maven("https://maven.aliyun.com/repository/public")
    gradlePluginPortal()
    mavenCentral()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
    maven("https://jitpack.io")
  }
}

rootProject.name = "demo-modules"
include("demo")
include("demo-hotfix")
