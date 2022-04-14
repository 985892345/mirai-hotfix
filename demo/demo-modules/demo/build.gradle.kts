plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("net.mamoe.mirai-console")
}

group = "com.ndhzs"
version = "1.0"

dependencies {

    // 下面是用于本地测试时依赖本地的 jar 文件
    fun File.child(name: String): File {
        return File(this, name)
    }
    // 这里需要使用 api，为了让热修的模块也能得到依赖
    api(
        fileTree(
            rootDir.parentFile.parentFile.child("build").child("libs").toPath()
        )
    )
}