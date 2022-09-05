import java.util.Properties
import java.io.FileInputStream

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



// 把密码写在 secret.properties 中
val secretPropsFile = project.rootProject.file("secret").resolve("secret.properties")
if (secretPropsFile.exists()) {
  println("Found secret props file, loading props")
  val properties = Properties()
  properties.load(FileInputStream(secretPropsFile))
  properties.forEach { name, value ->
    ext[name.toString()] = value
    println("$name=$value")
  }
} else {
  println("No props file, loading env vars")
}

tasks.register("javadocJar", Jar::class.java) {
  archiveClassifier.set("javadoc")
  from("javadoc")
}

tasks.register("sourcesJar", Jar::class.java) {
  archiveClassifier.set("sources")
  from(sourceSets["main"].allSource)
}

publishing {
  publications {
    create<MavenPublication>("MiraiHotfix") {
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
      artifact(tasks["javadocJar"])
      artifact(tasks["sourcesJar"])
      from(components["java"])
    }
    repositories {
      maven {
        url = uri("$buildDir/local")
      }
      maven {
        name = "mavenCentral" // 点击 publishMiraiHotfixPublicationToMavenCentralRepository 发布到 mavenCentral
        setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
          username = project.properties["ossrhUsername"].toString()
          password = project.properties["ossrhPassword"].toString()
        }
      }
    }
  }
}

signing {
  sign(publishing.publications["MiraiHotfix"])
}