plugins {
  kotlin("jvm") version "1.6.21"
  `java-gradle-plugin`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
}

gradlePlugin {
  plugins {
    create("testPlugin") {
      id = "test-plugin"
      implementationClass = "com.ndhzs.TestPlugin"
    }
  }
}