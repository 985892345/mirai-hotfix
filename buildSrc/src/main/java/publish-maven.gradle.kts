import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing

plugins {
  `maven-publish`
  signing
}

val publish = extensions.create("publish", Publish::class.java, project)
val sourceSets = extensions.getByName("sourceSets") as SourceSetContainer

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
      val projectArtifactId = publish.artifactId
      val projectGithubName = "mirai-hotfix"
      val projectDescription = publish.description
      val projectMainBranch = "master"
      create<MavenPublication>("release") {
        groupId = publish.groupId
        artifactId = projectArtifactId
        version = publish.version
        artifact(tasks["javadocJar"])
        artifact(tasks["sourcesJar"])
        from(components["java"])
        signing {
          sign(this@create)
        }

        pom {
          name.set(projectArtifactId)
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
          name = "build"
          url = buildDir.resolve("maven").toURI()
        }
        maven {
          // https://s01.oss.sonatype.org/
          name = "mavenCentral" // 点击 publishReleasePublicationToMavenCentralRepository 发布到 mavenCentral
          val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
          val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
          val isSnapshot = publish.version.toString().run {
            endsWith("SNAPSHOT")
          }
          setUrl(if (isSnapshot) snapshotsRepoUrl else releasesRepoUrl)
          credentials {
            username = project.properties["mavenCentralUsername"].toString()
            password = project.properties["mavenCentralPassword"].toString()
          }
        }
      }
    }
  }
}
