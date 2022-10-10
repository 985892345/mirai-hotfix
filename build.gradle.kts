plugins {
  val kotlinVersion = "1.7.20"
  val miraiVersion = "2.12.3"
  kotlin("jvm") version kotlinVersion apply false
  kotlin("plugin.serialization") version kotlinVersion apply false
  id("net.mamoe.mirai-console") version miraiVersion apply false
}
