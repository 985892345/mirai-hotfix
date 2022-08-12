import com.ndhzs.hotfix.HotfixKotlinPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/8/10 14:37
 */
class MiraiHotfix : HotfixKotlinPlugin(
  JvmPluginDescription(
    id = "com.ndhzs.mirai-hotfix-test",
    name = "测试 hotfix 的插件",
    version = "1.0",
  ) {
    author("985892345")
  }
) {

}