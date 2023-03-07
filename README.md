<div align="center">
    <h2>-QBot-</h2>
</div>

<div align="center">
    <small>&gt; 感谢JetBrains 提供的正版IDE支持&lt;</small>
</div>




<div align="center">
    <small>&gt;求星星～&lt;</small> 



 <a href="https://github.com/ForteScarlet/simpler-robot/releases/latest"><img alt="release" src="https://img.shields.io/github/v/release/Enderman-Teleporting/qbot" /></a>

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

下面是机器人具体配置部署方案

*注意：最新框架为v3，因此本仓库使用的v2框架有些老旧，所以会导致登录比较繁琐。当前v3框架的机器人正在使用Kotlin语言开发（详见v3-dev分支）*。以下就是如何配置机器人

# 机器人配置

## 1.环境配置

* Java17 <small>建议OpenJDK17   确保你在cmd（Windows） 终端（Linux）中能使用`java`命令</small>
* 7ZIP（Windows环境） Linux直接文件归档管理器就行
* 安卓软件：mirai login solver sakura （https://github.com/KasukuSakura/mirai-login-solver-sakura，进入Releases下载APK）（辅助滑动验证用）

## 2.登录配置

* 下载2d9qb12版本zip（进入https://github.com/Enderman-Teleporting/QBot/releases/tag/2d9qb2，点击QBot.zip即可下载）
* 解压zip
* 使用7zip/文件归档管理器打开launcher.jar和2d9qb12.jar 找到目录/BOOT-INF/simbot-bots/，编辑yourBot1.bot文件，将“账号”“密码”改为你QQ号、密码（如果被文字引号包围，千万不要删掉引号！）
* 在cmd/终端中`cd [你解压的位置]`
* 运行`java -jar launcher.jar`
* 运行后会弹出一个GUI 复制GUI第一栏中的网址 打开mirai login solver sakura 软件 输入这个网址 进行滑动验证 获取key复制 将key粘贴至GUI第二栏 回车
* 如果有短信登录/扫码登录，按照提示干就对了
* Ctrl+C结束程序运行
* 如果你不小心把cmd/终端窗口退了，你需要重新打开并且cd至你解压的位置

## 3.运行！

* 按以上步骤配置完后，cmd 运行

```
chcp 65001
java -Dmirai.no-desktop -Dfile.encoding=utf-8 -jar 2d9qb12.jar
```

Linux终端直接`java -Dmirai.no-desktop -Dfile.encoding=utf-8 -jar 2d9qb12.jar`

当你看到输出有你的帐号密码时，说明机器人已经启动成功了！

*注意：目录下生成的cache文件夹和device.json是包含登录信息，机器人配置和消息日志的重要文件，请妥善保管，不要丢了*

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

### 全局功能（可以在./cache/property.properties内修改）

- 日志写入

### 附加功能（可以在./cache/properties/机器人的QQ号修改）

- 二次元图片API(获取功能消息格式:二次元)  
 - 消息超300字视为刷屏禁言(需机器人管理权限)  
 - 修改群名称(需要机器人管理权限,获取功能格式:群名称 (注意这个空格)+你要的群名称)  
 - 自助群头衔(需机器人群主权限,获取功能格式:群头衔 (注意这个空格)+你要的头衔)  
 - 自助管理(需机器人群主权限,获取功能格式: 我要管理)(默认为false) 
 - MC图片API(获取功能消息格式:MC好图)(寄)  
 - MC服务器状态查询(获取功能消息格式:查服 IP:端口)(默认为false)

*注意：在此处的properties文件配置中含有很多项，此处列出的功能与properties文件中从上至下顺序相同*

# 问题反馈

在该仓库issues中提问  
或者加群讨论:1170443108 

# 支持作者

 害,我怎么会有赞助渠道,请给这个项目点亮你的星星吧!







网页:https://enderman-teleporting.github.io/QBot/
