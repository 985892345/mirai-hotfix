plugins {
  kotlin("jvm") version "1.7.10"
  `maven-publish`
  signing
}

repositories {
  maven("https://maven.aliyun.com/repository/public")
  mavenCentral()
}

dependencies {
  val miraiVersion = "2.12.2"
  compileOnly("net.mamoe:mirai-core:$miraiVersion") // mirai-core 的 API
  compileOnly("net.mamoe:mirai-console:$miraiVersion") // 后端
}

group = "io.github.985892345"
version = "1.4"

publishing {
  publications {
    create<MavenPublication>("maven") {
      pom {
        name.set("mirai-hotfix")
        description.set("用于实现简单的 miria 热修")
        url.set("https://github.com/985892345/mirai-hotfix")
    
        licenses {
          license {
            name.set("GNU Affero General Public License v3.0")
            url.set("https://github.com/985892345/mirai-hotfix/blob/master/LICENSE")
          }
        }
    
        developers {
          developer {
            id.set("985892345")
            name.set("GuoXiangrui")
            email.set("guo985892345@formail.com")
          }
        }
    
        scm {
          connection.set("https://github.com/985892345/mirai-hotfix.git")
          developerConnection.set("https://github.com/985892345/mirai-hotfix.git")
          url.set("https://github.com/985892345/mirai-hotfix")
        }
      }
  
      groupId = project.group.toString()
      artifactId  = "mirai-hotfix"
      version = project.version.toString()
      from(components["java"])
    }
    repositories {
      maven {
        url = uri("$buildDir/local")
      }
      maven {
        setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
          username = project.properties["username"].toString()
          password = project.properties["password"].toString()
        }
      }
    }
  }
}

signing {
  sign(publishing.publications["maven"])
}