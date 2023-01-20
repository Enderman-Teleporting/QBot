# QBot
### 这是作者的第二个项目,是一个QQ机器人  
 api骂人有点狠,稍微担待一下  
 ***注意:该项目仅供学习,娱乐等,禁止做违法的事  
 注意:该项目仅供学习,娱乐等,禁止做违法的事  
 注意:该项目仅供学习,娱乐等,禁止做违法的事  
 请详细阅读Wiki***
   
 ### 请先下载`Java`并确保加入了环境变量使指令可以使用  
  
 本项目使用的是**@ForteScarlet**的`simpler-robot`项目及其demo项目作为框架:  
 https://github.com/ForteScarlet/simple-robot-v2  
 https://github.com/ForteScarlet/simple-robot-demo-project  
 api接口为: 文本消息: 青云客(https://qingyunke.com) 图片:www.dmoe.cc (最近该接口寄了) 及 api.lantianyun.tk  
 查服SDK:https://github.com/FragLand/minestat  

 ### 机器人配制方法(IDE+打包)
 若要使用该机器人,请克隆库并且在`./src/main/resource`里面找到后缀名为`.bot`的文件并按提示输入账号密码  
 并且去IntelliJ Idea使用Springboot(详见simpler-robot文档,必须是springboot)打成jar包使用Java指令启动  
 示例启动指令:`java -Dfile.encoding=utf-8 -jar [路径]`  
 PS:必须要`-Dfile.encoding`,不然会乱码  
 cmd还需声明`chcp 65001`避免控制台乱码  
 同时你也可以选择示例jar包(见release)  
 ### 示例jar包的配制方法:
 使用7zip或Vim进入jar包的`/BOOT-INF/classes/simbot-bots/yourBot1.bot`文件按提示配制  

 如果遇到你的账号需要滑动登录,最好的选择是https://github.com/mzdluo123/TxCaptchaHelper ~~或 https://github.com/simple-robot/simbot-mirai-login-helper~~ (仅对Simbot v3有效)  

   
 # 包含功能 
 - 基础私聊
 - @机器人群聊  
 - 处理加群申请（需管理权限）并通报  
 - 处理加好友申请  
 - 群聊改名监听  
 - 群/私聊撤回监听  
 - 群成员权限变更  
 - 人云亦云（+1）功能
 - 呼叫回复  
 - 加群欢迎  
-----------------------以上为"基础功能",无法关闭------------------------------  
 - 日志写入  
-----------------------以上可在".\cache\property.properties"修改-------------------  
 - 二次元图片API(获取功能消息格式:二次元)  
 - 消息超300字视为刷屏禁言(需机器人管理权限)  
 - 修改群名称(需要机器人管理权限,获取功能格式:群名称 (注意这个空格)+你要的群名称)  
 - 自助群头衔(需机器人群主权限,获取功能格式:群头衔 (注意这个空格)+你要的头衔)  
 - 自助管理(需机器人群主权限,获取功能格式: 我要管理)(默认为false) 
 - MC图片API(获取功能消息格式:MC好图)  
 - MC服务器状态查询(获取功能消息格式:查服 IP:端口)(默认为false)
------以上可在".\cache\properties\机器人的QQ号.properties"修改,文件中各条与此处列的各条对应------
 ### 另有:
指令功能,请在控制台输入help
   
 ## 预计会实现的功能  
 [] 日程  
 [] 娱乐  
 [] 问答  
 [] 人云亦云功能(+1)代码层翻新   
 ...（这些看接下来v3的啦）  

## 注意
由于框架越来越老旧的原因,一旦你QQ机器人登陆成功,请妥善保管你的cache文件夹以及device.json,里面包含着重要的登录及设备信息,否则你再次登录将难以登录!    
  
  
 在该仓库issues中提问  
 或者加群讨论:1170443108  

## 支持作者
  害,我怎么会有赞助渠道,请给这个项目点亮你的星星吧!
  
  
  
 暂时simbot3的东西没摸透,摸透会换到simbot3.x

网页:https://enderman-teleporting.github.io/QBot/
