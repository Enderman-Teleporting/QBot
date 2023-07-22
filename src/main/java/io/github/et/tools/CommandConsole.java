package io.github.et.tools;

import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import net.mamoe.mirai.Bot;

import java.io.Console;
import java.util.Objects;

public class CommandConsole {
    public static String getCommand(){
        Console console=System.console();
        return console.readLine();
    }
    public static void handle(Bot bot) throws LoggerNotDeclaredException, LevelNotMatchException {
        String cmd=getCommand();
        String[] commands=cmd.split(" ");
        Logger logger= Logger.getDeclaredLogger();
        try{
            switch (commands[0]) {
                case "sendGroupMsg" -> {
                    StringBuilder totalMsg = new StringBuilder();
                    for (int i = 2; i < commands.length; i++) {
                        totalMsg.append(commands[i]).append(" ");
                    }
                    Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).sendMessage(totalMsg.toString());
                    logger.fine("Group Message Has Been Sent!");
                }
                case "sendPrivateMsg" -> {
                    StringBuilder Msg = new StringBuilder();
                    for (int i = 2; i < commands.length; i++) {
                        Msg.append(commands[i]).append(" ");
                    }
                    Objects.requireNonNull(bot.getFriend(Long.parseLong(commands[1]))).sendMessage(Msg.toString());
                    logger.fine("Private Message Has Been Sent!");
                }
                case "setAdmin" -> {
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).modifyAdmin(true);
                    logger.fine(commands[2] + " Has Been Set As An Admin!");
                }
                case "cancelAdmin" -> {
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).modifyAdmin(false);
                    logger.fine(commands[2] + " Is No Longer An Admin!");
                }
                case "deleteFriend" -> {
                    Objects.requireNonNull(bot.getFriend(Long.parseLong(commands[1]))).delete();
                    logger.fine(commands[1] + " Is No Longer Your Friend");
                }
                case "deleteGroup" -> {
                    Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).quit();
                    logger.fine("You Left The Group " + commands[1]);
                }
                case "mute" -> {
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).mute(Integer.parseInt(commands[3]));
                    logger.fine(commands[2] + " Is Muted!");
                }
                case "groupName" -> {
                    Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).setName(commands[2]);
                    logger.fine("The Group Name Has Been Changed Into " + commands[2]);
                }
                case "kick" -> {
                    StringBuilder reason = new StringBuilder();
                    for (int i = 3; i < commands.length; i++) {
                        reason.append(commands[i]).append(" ");
                    }
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).kick(reason.toString());
                    logger.fine(commands[2] + " Has Been Kicked");
                }
                case "help" -> logger.fine("""
                        sendGroupMsg [GroupCode] [Message]
                        sendPrivateMsg [FriendCode] [Message]
                        setAdmin [GroupCode] [MemberCode]
                        cancelAdmin [GroupCode] [MemberCode]
                        deleteFriend [FriendCode]
                        deleteGroup [GroupCode]
                        mute [GroupCode] [MemberCode] [Time(seconds)]
                        groupName [GroupCode] [NewName]
                        kick [GroupCode] [MemberCode] [Reason]
                        help
                        """);
                default -> logger.fine("Unknown Command, Type \"help\" For Help");
            }
        }catch(Exception e){
            logger.fine("The Command Is Wrong, Type \"help\" For Help\n"+e);
        }

    }
}
