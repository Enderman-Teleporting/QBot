package com.tools;

/*package com.bot.qbot.tools;



import java.io.Console;
@Beans
public class cmd {
    public static String getCommand(){
        Console console=System.console();
        String command=console.readLine();
        return command;
    }
    public static void Handler(Bot bot){
        String cmd=getCommand();
        String[] commands=cmd.split(" ");
        try{
            switch (commands[0]) {
                case "sendGroupMsg":
                    StringBuilder totalMsg = new StringBuilder();
                    for (int i = 2; i < commands.length; i++) {
                        totalMsg.append(commands[i]).append(" ");
                    }
                    bot.getSender().SENDER.sendGroupMsg(commands[1], totalMsg.toString());
                    System.out.println("Group Message Has Been Sent!");
                    break;
                case "sendPrivateMsg":
                    StringBuilder Msg = new StringBuilder();
                    for (int i = 2; i < commands.length; i++) {
                        Msg.append(commands[i]).append(" ");
                    }
                    bot.getSender().SENDER.sendPrivateMsg(commands[1], Msg.toString());
                    System.out.println("Private Message Has Been Sent!");
                    break;
                case "setAdmin":
                    bot.getSender().SETTER.setGroupAdmin(commands[1], commands[2], true);
                    System.out.println(commands[2] + " Has Been Set As An Admin!");
                    break;
                case "cancelAdmin":
                    bot.getSender().SETTER.setGroupAdmin(commands[1], commands[2], false);
                    System.out.println(commands[2] + " Is No Longer An Admin!");
                    break;
                case "deleteFriend":
                    bot.getSender().SETTER.setFriendDelete(commands[1]);
                    System.out.println(commands[1] + " Is No Longer Your Friend");
                    break;
                case "deleteGroup":
                    bot.getSender().SETTER.setGroupQuit(commands[1], true);
                    System.out.println("You Left The Group " + commands[1]);
                    break;
                case "mute":
                    bot.getSender().SETTER.setGroupBan(commands[1], commands[2], Integer.parseInt(commands[3]));
                    System.out.println(commands[2] + " Is Muted!");
                    break;
                case "setGroupMute":
                    bot.getSender().SETTER.setGroupWholeBan(commands[1], true);
                    System.out.println("The Whole Group Is Muted!");
                    break;
                case "cancelGroupMute":
                    bot.getSender().SETTER.setGroupWholeBan(commands[1], false);
                    System.out.println("You Have Cancelled The Group Mute!");
                    break;
                case "groupName":
                    bot.getSender().SETTER.setGroupName(commands[1], commands[2]);
                    System.out.println("The Group Name Has Been Changed Into " + commands[2]);
                    break;
                case "kick":
                    StringBuilder reason = new StringBuilder();
                    for (int i = 3; i < commands.length; i++) {
                        reason.append(commands[i]).append(" ");
                    }
                    bot.getSender().SETTER.setGroupMemberKick(commands[1], commands[2], reason.toString(), false);
                    System.out.println(commands[2] + " Was Kicked");
                    break;
                case "ban":
                    StringBuilder rs = new StringBuilder();
                    for (int i = 3; i < commands.length; i++) {
                        rs.append(commands[i]).append(" ");
                    }
                    bot.getSender().SETTER.setGroupMemberKick(commands[1], commands[2], rs.toString(), true);
                    System.out.println(commands[2] + " Was Banned From The Group");
                    break;
                case "help":
                    System.out.println("sendGroupMsg [GroupCode] [Message]\n" +
                            "sendPrivateMsg [FriendCode] [Message]\n" +
                            "setAdmin [GroupCode] [MemberCode]\n" +
                            "cancelAdmin [GroupCode] [MemberCode]\n" +
                            "deleteFriend [FriendCode]\n" +
                            "deleteGroup [GroupCode]\n" +
                            "mute [GroupCode] [MemberCode] [Time(seconds)]\n" +
                            "setGroupMute [GroupCode]\n" +
                            "cancelGroupMute [GroupCode]\n" +
                            "groupName [GroupCode] [NewName]\n" +
                            "kick [GroupCode] [MemberCode] [Reason]\n" +
                            "ban [GroupCode] [Member] [Reason]\n" +
                            "help");
                    break;
                default:
                    System.out.println("Unknown Command, Type \"help\" For Help");
                    break;
            }
        }catch(Exception e){
            System.out.println("The Command Is Wrong, Type \"help\" For Help\n"+e);
        }

    }
}

 */
public class cmd {
}