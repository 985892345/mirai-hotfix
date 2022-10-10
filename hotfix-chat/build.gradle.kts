plugins {
  kotlin("jvm")
  id("publish-maven")
}

dependencies {
  implementation(project(":hotfix-base"))
}

publish {
  projectArtifact = "mirai-hotfix-chat"
  projectDescription = "基于 mirai-console，用于实现简单的逻辑代码热修"
}