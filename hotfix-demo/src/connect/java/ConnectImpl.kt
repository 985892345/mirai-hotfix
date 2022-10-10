import com.google.gson.Gson
import com.ndhzs.IConnect
import net.mamoe.mirai.console.command.CommandSender

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
//    return "123"
//    return "abc"
    return gson.toString()
  }

  override fun CommandSender.onFixLoad() {
  }

  override fun CommandSender.onFixUnload(): Boolean {
    return true
  }
}