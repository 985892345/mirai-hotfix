import java.util.Properties
import java.io.FileInputStream

plugins {
  kotlin("jvm")
  id("net.mamoe.mirai-console")
  `maven-publish`
  signing
}

group = rootProject.group
version = rootProject.version

// 把密码写在 secret.properties 中
val secretPropsFile = rootDir.resolve("secret").resolve("secret.properties")
if (secretPropsFile.exists()) {
  println("Found secret props file, loading props")
  val properties = Properties()
  properties.load(FileInputStream(secretPropsFile))
  properties.forEach { name, value ->
    ext[name.toString()] = value
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
      
      groupId = project.group.toString()
      artifactId = "mirai-hotfix"
      version = project.version.toString()
      artifact(tasks["javadocJar"])
      artifact(tasks["sourcesJar"])
      from(components["java"])
      
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
    }
    repositories {
      maven {
        url = uri("$buildDir/local")
      }
      maven {
        name = "mavenCentral" // 点击 publishMiraiHotfixPublicationToMavenCentralRepository 发布到 mavenCentral
        val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
        val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
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