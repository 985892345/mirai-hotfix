import java.util.Properties

plugins {
  val kotlinVersion = "1.7.10"
  val miraiVersion = "2.12.2"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  id("net.mamoe.mirai-console") version miraiVersion apply false
}

val properties = Properties().apply {
  load(rootDir.resolve("version.properties").inputStream())
}

group = properties.getValue("group")
version = properties.getValue("version")