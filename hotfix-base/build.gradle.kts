plugins {
  kotlin("jvm")
  id("net.mamoe.mirai-console")
  id("publish-maven")
}

publish {
  projectArtifact = "mirai-hotfix-base"
  projectDescription = "基于 mirai-console，用于实现简单的逻辑代码热修"
}
