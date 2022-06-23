pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "SourceSetPluginTest"
include("module-test")
includeBuild("gardle-plugin")
