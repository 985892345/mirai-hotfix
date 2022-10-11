plugins {
  kotlin("jvm")
  id("net.mamoe.mirai-console")
  id("publish-maven")
}

publish {
  artifactId = "mirai-hotfix"
  description = "基于 mirai-console，用于实现简单的逻辑代码热修"
}
