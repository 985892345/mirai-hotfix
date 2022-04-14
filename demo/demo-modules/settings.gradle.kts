pluginManagement {
  repositories {
    maven("https://maven.aliyun.com/repository/public")
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "demo-modules"
include("demo")
include("demo-hotfix")
