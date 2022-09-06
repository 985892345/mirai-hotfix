import java.util.Properties
import java.io.FileInputStream

plugins {
  val kotlinVersion = "1.7.10"
  val miraiVersion = "2.12.2"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  id("net.mamoe.mirai-console") version miraiVersion apply false
}

val properties = Properties().apply {
  load(FileInputStream(rootDir.resolve("version.properties")))
}

group = properties.getValue("group")
version = properties.getValue("version")