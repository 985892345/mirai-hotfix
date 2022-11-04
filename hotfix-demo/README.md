# 热修示例 demo
 此 demo 采用新增源集来使用热修，接入的逻辑很简单，主要分为一下步骤：

## 1、接入插件
![Maven Central](https://img.shields.io/maven-central/v/io.github.985892345/mirai-hotfix)
```kotlin
plugins {
  id("io.github.985892345.mirai-hotfix") version "xxx" // 版本号请查看标签
}

// 如果你使用的 2.11 及版本以上的 Mirai Console Gradle 打包插件，请添加上如下代码
// 原因：https://github.com/mamoe/mirai/tree/dev/mirai-console/tools/gradle-plugin
dependencies {
  shadowLink("io.github.985892345:mirai-hotfix")
}
```

### 2、新增源集
在 `build.gradle.kts` 中书写如下代码：
```kotlin
hotfix {
  // 比如新增一个名字叫 connect 的源集
  createHotfix("connect")
}

// 如果你需要给 connect 源集单独使用依赖，可以采用下面这种写法
hotfix {
  // 这里用于设置自定义源集并引入该源集单独使用的依赖
  createHotfix("connect") {
    implementation("com.google.code.gson:gson:2.9.0")
  }
}
```
注意：新增源集会自动依赖 `main` 源集，不必重复添加依赖

### 3、编写接口
你需要定义接口来分离逻辑代码，然后将逻辑代码写在 `connect` 源集中  
具体方式如下：
```kotlin
// 在 main 源集中定义一个接口，必须实现 JarEntrance 接口
// 注意：为了让该接口更方便寻找，IConnect 只能写在根目录下！
interface IConnect : JarEntrance {
  fun get(): String
}

// 在 connect 源集中 java 根目录下实现该接口
class ConnectImpl : IConnect {
  
  override fun get(): String {
    hotfixCoroutineScope.launch {
      // 使用 hotfixCoroutineScope 开协程可以在卸载时自动取消子协程
    }
    return "123"
  }
  
  // 热修加载时的回调
  // 注意：请使用 HotfixCommandSender 开协程，因为该协程会在卸载时自动取消子协程。suspend 也是同理
  override suspend fun HotfixCommandSender.onFixLoad() {}
  
  // 卸载时的回调，返回值决定了能否支持卸载
  // 注意：请使用 HotfixCommandSender 开起协程，因为该协程会在卸载时自动取消子协程。suspend 也是同理
  override suspend fun HotfixCommandSender.onFixUnload(): Boolean = true
}
```

### 4、打包源集
新增 `connect` 源集后会同时增加 gradle 打包任务，会输出 jar 包到 build/libs 下
```
./gradlew hotfix-connect
```

### 5、进行热修
#### 5.1、手动命令热修
上传到你服务器的 Mirai控制台目录/hotfix/... 中，三个点取决于你的插件 id 后缀
```
- /bots
- /config
- /data
- /hotfix
--- /demo-sourceSet           这是 hotfixDirName，你需要把文件部署在该目录下，再调用热修命令
------ /.run                  这是热修成功并且正在运行的文件，一般不需要进入查看
------ hotfix-connect.jar     这是部署后即将进行热修的文件，使用 fix... reload hotfix-connect.jar 命令进行热修
- /plugins
```

然后在控制台中输入以下指令进行热修
```
fix... reload hotfix-connect.jar 
// 该三个点取决于你的 id 后缀，后面文件名称支持正则表达式，也可以直接不写，寻找所有文件
```

#### 5.2、群文件热修
在群聊中输入：
```
fix... chat
```
输入后会监听你发的下一条消息，但只监听一次，你只需要把热修包丢在群里即可自动热修，发送其他东西就取消监听。  
下载成功后默认会撤回群文件（如果是管理员的话）

