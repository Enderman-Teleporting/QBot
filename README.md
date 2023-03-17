<div align="center">
    <h2>-QBot-</h2>
</div>

<div align="center">
    <small>&gt; 感谢JetBrains 提供的正版IDE支持&lt;</small>
</div>




<div align="center">
    <small>&gt;求星星～&lt;</small> 



 <a href="https://github.com/Enderman-Teleporting/qbot/releases/latest"><img alt="release" src="https://img.shields.io/github/v/release/Enderman-Teleporting/qbot" /></a>

   <hr>
   <img alt="stars" src="https://img.shields.io/github/stars/Enderman-Teleporting/qbot" />
   <img alt="forks" src="https://img.shields.io/github/forks/Enderman-Teleporting/qbot" />
   <img alt="watchers" src="https://img.shields.io/github/watchers/Enderman-Teleporting/qbot" />
   <img alt="repo-size" src="https://img.shields.io/github/repo-size/Enderman-Teleporting/qbot" />

   <img alt="issues" src="https://img.shields.io/github/issues-closed/Enderman-Teleporting/QBot?color=green" />
   <img alt="last-commit" src="https://img.shields.io/github/last-commit/Enderman-Teleporting/qbot" />
   <img alt="top-language" src="https://img.shields.io/github/languages/top/Enderman-Teleporting/qbot" />
<a href="./COPYING"><img alt="copying" src="https://img.shields.io/github/license/Enderman-Teleporting/qbot" /></a>
</div>
这是由Simpler-Robot框架开发的一个QQ机器人，仅供交流学习娱乐使用。  

该项目聊天功能接入的API骂人比较狠，请稍微担待下

> 请采用最新版本，即便是开发版，这样的话登录会更方便

# 机器人配置

## 1.环境配置

* Java17 
* git (以运行`git clone `指令)
* 一个好用的Java IDE（建议Intellij Idea)
* 7ZIP（Windows环境） Linux直接文件归档管理器就行
* 安卓软件：mirai login solver sakura （https://github.com/KasukuSakura/mirai-login-solver-sakura ，进入Releases下载APK）（辅助滑动验证用）

## 2.登录、启动操作

* 拉下项目

  ```powershell
  cd [你要存的文件的位置]
  git clone [项目地址]
  ```

* 打开IDE，设置启动类为`com.bot.qbot.SimbotExampleApplicationKt`

* 在resources文件夹下simbot-bots/yourBot1.bot

* 将`Your Code` 改为你的账号，将`Your Password`改为密码，`Your Password`被引号包围，请保留引号

* 点击运行

* 运行后会输出许多东西,复制最下面的网址网址 打开mirai login solver sakura 软件 输入这个网址 进行滑动验证 获取ticket复制 将ticket粘贴至控制台 回车

* 如果有短信登录/扫码登录，按照提示干就对了

* 当你看到输出`Bot Registration Successful!`时，说明机器人已经启动成功了！

> 注意：目录下生成的cache文件夹和device.json是包含登录信息，机器人配置和消息日志的重要文件，请妥善保管，不要丢了

# 机器人功能：

### 基础功能（无法关闭）

- 基础私聊
- 被@之后的群聊（注意一定不是文字的@）
- 处理加群申请（需管理权限）并在群内通报
- 处理加好友申请
- 群聊改名监听
- 群/私聊撤回监听
- 群成员权限变更
- 人云亦云（+1）功能
- 呼叫（机器人昵称）回复
- 新人欢迎
- 退群监听
- ~~控制台命令~~（v3暂寄)
- 消息统计(发送"消息统计")

### 全局功能（可以在./cache/properties/property.properties内修改）

- 日志写入

### 附加功能（可以在./cache/properties/机器人的QQ号.properties修改）

- 二次元图片API(获取功能消息格式:二次元)  
 - 消息超300字视为刷屏禁言(需机器人管理权限)  
 - ~~修改群名称(需要机器人管理权限,获取功能格式:群名称 (注意这个空格)+你要的群名称)~~ （v3暂寄）
 - ~~自助群头衔(需机器人群主权限,获取功能格式:群头衔 (注意这个空格)+你要的头衔)~~?(v3可能寄)
 - ~~自助管理(需机器人群主权限,获取功能格式: 我要管理)(默认为false)~~？（v3可能寄）
 - MC图片API(获取功能消息格式:MC好图) 
 - MC服务器状态查询(获取功能消息格式:查服 IP:端口)(默认为false)

> 注意：在此处的properties文件配置中含有很多项，此处列出的功能与properties文件中从上至下顺序相同

# 问题反馈

在该仓库issues中提问  
或者加群讨论:1170443108 

# 支持作者

 害,我怎么会有赞助渠道,请给这个项目点亮你的星星吧!







网页:https://enderman-teleporting.github.io/QBot/
