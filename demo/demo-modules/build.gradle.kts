plugins {
    val kotlinVersion = "1.6.20"
    val miraiVersion = "2.10.1"
    kotlin("jvm") version  kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
    id("net.mamoe.mirai-console") version miraiVersion apply false
}