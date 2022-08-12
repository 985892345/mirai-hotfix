plugins {
  kotlin("jvm") version "1.7.10"
  `maven-publish`
}

repositories {
  maven("https://maven.aliyun.com/repository/public")
  mavenCentral()
}

dependencies {
  val miraiVersion = "2.12.1"
  compileOnly("net.mamoe:mirai-core:$miraiVersion") // mirai-core 的 API
  compileOnly("net.mamoe:mirai-console:$miraiVersion") // 后端
}

group = "io.github.985892345"
version = "1.3"

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
    }
    repositories {
      maven {
        url = uri("$buildDir/local")
      }
    }
  }
}