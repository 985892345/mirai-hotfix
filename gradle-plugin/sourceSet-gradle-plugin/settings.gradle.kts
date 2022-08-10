pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "sourceSet-gradle-plugin"
include("module-test")
includeBuild("gradle-plugin")
