# mirai-hotfix
 基于 Mirai Console 实现的热修插件

 mirai 项目：https://github.com/mamoe/mirai
 
## 使用方式
 请看 [demo](demo)

## 功能
- [x] 通过 jar 包进行热修

## 未来改进
- [ ] 热修命令（想尝试一下）
- [ ] 实现 java 插件的热修（目前只能 kotlin）
- [ ] ......

 欢迎各位在 issues 提出建议
 
## 实现思路
 使用接口来实现 定义命令的插件代码 与 逻辑实现代码 的分离，从而可以随时替换逻辑代码，
 在没有修改底层代码而实现简单的热修