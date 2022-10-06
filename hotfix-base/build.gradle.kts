plugins {
  kotlin("jvm")
  id("net.mamoe.mirai-console")
  `maven-publish`
  signing
}

group = rootProject.group
version = rootProject.version
val projectArtifact = "mirai-hotfix"
val projectGithubName = projectArtifact
val projectDescription = "基于 mirai-console，用于实现简单的逻辑代码热修"
val projectMainBranch = "master"

tasks.register("javadocJar", Jar::class.java) {
  archiveClassifier.set("javadoc")
  from("javadoc")
}

tasks.register("sourcesJar", Jar::class.java) {
  archiveClassifier.set("sources")
  from(sourceSets["main"].allSource)
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("release") {
        groupId = project.group.toString()
        artifactId = projectArtifact
        version = project.version.toString()
        artifact(tasks["javadocJar"])
        artifact(tasks["sourcesJar"])
        from(components["java"])
        signing {
          sign(this@create)
        }
        
        pom {
          name.set(projectArtifact)
          description.set(projectDescription)
          url.set("https://github.com/985892345/$projectGithubName")
          
          licenses {
            license {
              name.set("GNU Affero General Public License v3.0")
              url.set("https://github.com/985892345/$projectGithubName/blob/$projectMainBranch/LICENSE")
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
            connection.set("https://github.com/985892345/$projectGithubName.git")
            developerConnection.set("https://github.com/985892345/$projectGithubName.git")
            url.set("https://github.com/985892345/$projectGithubName")
          }
        }
      }
      repositories {
        maven {
          // https://s01.oss.sonatype.org/
          name = "mavenCentral" // 点击 publishReleasePublicationToMavenCentralRepository 发布到 mavenCentral
          val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
          val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
          setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
          credentials {
            username = project.properties["mavenCentralUsername"].toString()
            password = project.properties["mavenCentralPassword"].toString()
          }
        }
      }
    }
  }
}
