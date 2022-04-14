plugins {
    kotlin("jvm") version "1.6.20"
    id("maven-publish")
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    val miraiVersion = "2.10.1"
    compileOnly("net.mamoe:mirai-core:$miraiVersion") // mirai-core 的 API
    compileOnly("net.mamoe:mirai-console:$miraiVersion") // 后端
}

group = "com.ndhzs"
version = "0.1"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}