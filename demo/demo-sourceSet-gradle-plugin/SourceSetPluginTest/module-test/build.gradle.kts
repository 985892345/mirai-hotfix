plugins {
  kotlin("jvm")
  id("test-plugin")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
}

test {
  addConfig("demo")
}