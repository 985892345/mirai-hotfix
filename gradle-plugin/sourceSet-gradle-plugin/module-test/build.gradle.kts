plugins {
  kotlin("jvm")
  id("io.github.985892345.mirai-hotfix")
  id("net.mamoe.mirai-console") version "2.12.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
}

hotfix {
  createHotfix("hotfix")
}