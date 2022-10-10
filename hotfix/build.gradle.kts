plugins {
  kotlin("jvm")
  id("publish-maven")
}

dependencies {
  implementation(project(":hotfix-base"))
  implementation(project(":hotfix-chat"))
}

publish {
  projectArtifact = "mirai-hotfix"
  projectDescription = "基于 mirai-console，用于实现简单的逻辑代码热修"
}