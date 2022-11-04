import com.google.gson.Gson
import com.ndhzs.IConnect
import com.ndhzs.hotfix.HotfixKotlinPlugin
import com.ndhzs.hotfix.comand.HotfixCommandSender
import com.ndhzs.hotfix.suffix.jar.hotfixCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 实现在 main 源集中定义的 [IConnect] 接口
 *
 * 在热修成功后即可进行通信
 *
 * ## 注意
 * 启动接口必须放到根目录下
 */
class ConnectImpl : IConnect {

  val gson = Gson()

  override fun get(): String {
    hotfixCoroutineScope?.launch {
    
    }
    return "123"
//    return "abc"
  }
  
  override suspend fun HotfixCommandSender.onFixLoad(plugin: HotfixKotlinPlugin) {
    launch {
      while (true) {
        delay(3000)
        plugin.logger.info(get())
      }
    }
  }
  
  override suspend fun HotfixCommandSender.onFixUnload(plugin: HotfixKotlinPlugin): Boolean {
    return true
  }
}