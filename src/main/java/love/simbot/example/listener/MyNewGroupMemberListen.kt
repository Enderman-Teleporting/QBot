package love.simbot.example.listener

import love.forte.common.ioc.annotation.Beans
import love.forte.common.ioc.annotation.Depend
import love.forte.simbot.annotation.Listen
import love.forte.simbot.annotation.OnGroupMemberIncrease
import love.forte.simbot.api.message.MessageContent
import love.forte.simbot.api.message.MessageContentBuilderFactory
import love.forte.simbot.api.message.containers.AccountInfo
import love.forte.simbot.api.message.events.GroupMemberIncrease
import love.forte.simbot.api.message.events.GroupMemberPermissionChanged
import love.forte.simbot.api.message.events.GroupNameChanged
import love.forte.simbot.api.sender.Sender
import love.simbot.example.Log_settler.Companion.writelog
import java.io.IOException

@Beans
class MyNewGroupMemberListen {
    @Depend
    private val messageBuilderFactory: MessageContentBuilderFactory? = null
    @OnGroupMemberIncrease
    @Throws(IOException::class)
    fun newGroupMember(groupMemberIncrease: GroupMemberIncrease, sender: Sender) {
        val builder = messageBuilderFactory!!.getMessageContentBuilder()
        val accountInfo = groupMemberIncrease.accountInfo
        val msg = builder
                .at(accountInfo)
                .text(" 欢迎萌新!我是机器人菲菲 很高兴见到你\n")
                .build()
        val groupInfo = groupMemberIncrease.groupInfo
        sender.sendGroupMsg(groupInfo, msg)
        writelog("OnGroup$groupInfo")
        writelog(groupMemberIncrease.botInfo.toString())
        writelog("""
    $msg
    
    
    
    """.trimIndent())
    }

    @Listen(GroupMemberPermissionChanged::class)
    @Throws(IOException::class)
    fun permissionChange(groupMemberPermissionChanged: GroupMemberPermissionChanged, sender: Sender) {
        val accountInfoC = groupMemberPermissionChanged.accountInfo
        val operatorinfo: AccountInfo? = groupMemberPermissionChanged.operatorInfo
        val groupInfoC = groupMemberPermissionChanged.groupInfo
        val builder1 = messageBuilderFactory!!.getMessageContentBuilder()
        val permissionlose = groupMemberPermissionChanged.isLostManagementRights
        val permissionget = groupMemberPermissionChanged.isGetManagementRights
        var message_: MessageContent
        if (permissionlose) {
            message_ = builder1
                    .at(accountInfoC)
                    .text("的权限被")
                    .at(operatorinfo!!)
                    .text("降级为群成员权限")
                    .build()
            sender.sendGroupMsg(groupInfoC, message_)
            writelog("OnGroup$groupInfoC")
            writelog(groupMemberPermissionChanged.botInfo.toString())
            writelog("""
    $message_
    
    
    
    """.trimIndent())
        }
        if (permissionget) {
            message_ = builder1
                    .at(accountInfoC)
                    .text("的权限被")
                    .at(operatorinfo!!)
                    .text("升级为管理权限")
                    .build()
            sender.sendGroupMsg(groupInfoC, message_)
            writelog("OnGroup$groupInfoC")
            writelog(groupMemberPermissionChanged.botInfo.toString())
            writelog("""
    $message_
    
    
    
    """.trimIndent())
        }
    }

    @Listen(GroupNameChanged::class)
    @Throws(IOException::class)
    fun groupnamechange(groupNameChanged: GroupNameChanged, sender: Sender) {
        val accountInfoGMC: AccountInfo? = groupNameChanged.operatorInfo
        val groupInfoGMC = groupNameChanged.groupInfo
        val groupname = groupNameChanged.afterChange
        val groupnamebef = groupNameChanged.beforeChange
        val builder2 = messageBuilderFactory!!.getMessageContentBuilder()
        val message1 = builder2
                .at(accountInfoGMC!!)
                .text("将群名称由")
                .text(groupnamebef!!)
                .text("改为")
                .text(groupname!!)
                .build()
        sender.sendGroupMsg(groupInfoGMC, message1)
        writelog("OnGroup$groupInfoGMC")
        writelog(groupNameChanged.botInfo.toString())
        writelog("""
    $message1
    
    
    
    """.trimIndent())
    }
}