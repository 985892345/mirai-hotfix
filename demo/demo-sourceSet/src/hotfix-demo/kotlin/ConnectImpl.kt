import com.google.gson.Gson
import com.ndhzs.IConnect
import net.mamoe.mirai.console.command.CommandSender

/**
 * 这是热修文件的启动类
 */
class ConnectImpl : IConnect {

  val gson = Gson()

  override fun get(): String {
//    return "123"
//    return "abc"
    return gson.toString()
  }

  override suspend fun CommandSender.onFixLoad() {
  }

  override suspend fun CommandSender.onFixUnload(): Boolean {
    return true
  }
}