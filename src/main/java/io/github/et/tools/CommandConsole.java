package io.github.et.tools;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;
import io.github.et.subprocessLoader.ServerStream;
import io.github.et.utils.json.JsonBuilder;
import io.github.et.conopt4j.logger.Logger;
import net.mamoe.mirai.Bot;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CommandConsole {
    public static String getCommand(){
        return Main.console.readLine();
    }
    public static String handle(Bot bot,String cmd) {
        String[] commands=cmd.split(" ");
        try{
            switch (commands[0]) {
                case "sendGroupMsg" -> {
                    StringBuilder totalMsg = new StringBuilder();
                    for (int i = 2; i < commands.length; i++) {
                        totalMsg.append(commands[i]).append(" ");
                    }
                    Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).sendMessage(totalMsg.toString());
                    return("Group Message Has Been Sent!");
                }
                case "sendPrivateMsg" -> {
                    StringBuilder Msg = new StringBuilder();
                    for (int i = 2; i < commands.length; i++) {
                        Msg.append(commands[i]).append(" ");
                    }
                    Objects.requireNonNull(bot.getFriend(Long.parseLong(commands[1]))).sendMessage(Msg.toString());
                    return("Private Message Has Been Sent!");
                }
                case "setAdmin" -> {
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).modifyAdmin(true);
                    return(commands[2] + " Has Been Set As An Admin!");
                }
                case "cancelAdmin" -> {
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).modifyAdmin(false);
                    return(commands[2] + " Is No Longer An Admin!");
                }
                case "deleteFriend" -> {
                    Objects.requireNonNull(bot.getFriend(Long.parseLong(commands[1]))).delete();
                    return(commands[1] + " Is No Longer Your Friend");
                }
                case "deleteGroup" -> {
                    Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).quit();
                    return("You Left The Group " + commands[1]);
                }
                case "mute" -> {
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).mute(Integer.parseInt(commands[3]));
                    return(commands[2] + " Is Muted!");
                }
                case "groupName" -> {
                    Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).setName(commands[2]);
                    return("The Group Name Has Been Changed Into " + commands[2]);
                }
                case "kick" -> {
                    StringBuilder reason = new StringBuilder();
                    for (int i = 3; i < commands.length; i++) {
                        reason.append(commands[i]).append(" ");
                    }
                    Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(Long.parseLong(commands[1]))).get(Long.parseLong(commands[2]))).kick(reason.toString());
                    return(commands[2] + " Has Been Kicked");
                }
                case "set"->{
                    JSONObject jo1= Main.JSON_ALL;
                    JSONObject jo2=Main.JSON_NO_GUIDE;
                    if(commands[1].matches("[0-9]+$")){
                        String[] path=commands[2].split("\\.");
                        for (String s : path) {
                            jo1 = jo1.getJSONObject(s);
                            jo2 = jo2.getJSONObject(s);
                        }
                        String name=path[path.length-1];
                        name=name.replaceFirst(String.valueOf(name.charAt(0)),String.valueOf(name.charAt(0)).toLowerCase());
                        if(jo1.getBoolean(name)){
                            long id = Long.parseLong(commands[1]);
                            JSONArray exclude1 = jo1.getJSONArray("exclude");
                            JSONArray exclude2 = jo2.getJSONArray("exclude");

                            if (!(exclude1.contains((int)id)||exclude1.contains(id))) {
                                exclude1.add((int)id);
                                exclude2.add((int)id);
                            } else {
                                exclude1.remove((Integer) (int) id);
                                exclude2.remove((Integer) (int) id);
                                exclude1.remove(id);
                                exclude2.remove(id);
                            }
                        }else{
                            long id = Long.parseLong(commands[1]);
                            JSONArray include1 = jo1.getJSONArray("include");
                            JSONArray include2 = jo2.getJSONArray("include");

                            if (!(include1.contains((int)id)||include1.contains(id))) {
                                include1.add((int)id);
                                include2.add((int)id);
                            } else {
                                include1.remove((Integer) (int) id);
                                include2.remove((Integer) (int) id);
                                include1.remove(id);
                                include2.remove(id);
                            }
                        }
                    }else{
                        String[] path= commands[1].split("\\.");
                        for (String s : path) {
                            jo1 = jo1.getJSONObject(s);
                            jo2 = jo2.getJSONObject(s);
                        }
                        String name = commands[2];
                        if(jo1.get(name) instanceof Boolean){
                            jo1.put(name,Boolean.valueOf(commands[3]));
                            jo2.put(name,Boolean.valueOf(commands[3]));
                        } else if (jo1.get(name) instanceof Integer) {
                            jo1.put(name, Integer.parseInt(commands[3]));
                            jo2.put(name, Integer.parseInt(commands[3]));
                        } else if (jo1.get(name) instanceof String) {
                            StringBuilder value = new StringBuilder();
                            for (int i = 3; i < commands.length; i++) {
                                value.append(commands[i]).append(" ");
                            }
                            String finalValue = value.toString().trim();
                            jo1.put(name, finalValue);
                            jo2.put(name, finalValue);
                        } else if (jo1.get(name) instanceof JSONArray) {
                            JSONArray newArray = com.alibaba.fastjson2.JSON.parseArray("[" + commands[3] + "]");
                            jo1.put(name, newArray);
                            jo2.put(name, newArray);
                        }
                    }
                    JsonBuilder.update();
                    return "setting completed!";
                }
                case "restart" -> {
                    if(commands.length==1){
                        if (ServerStream.os != null) {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                            bw.write("restart");
                            bw.newLine();
                            bw.flush();
                        }
                    }else {
                        if (ServerStream.os != null) {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                            bw.write(commands[0] + " " + commands[1]);
                            bw.newLine();
                            bw.flush();
                        }
                    }
                    return "restarting...";
                }
                case "forceStop" -> {
                    if(ServerStream.os!=null){
                        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(ServerStream.os,StandardCharsets.UTF_8));
                        bw.write(commands[0]+" "+commands[1]);
                        bw.newLine();
                        bw.flush();
                    }
                    return "shutting...";
                }
                case "startup" -> {
                    if(ServerStream.os!=null){
                        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(ServerStream.os,StandardCharsets.UTF_8));
                        bw.write(commands[0]+" "+commands[1]);
                        bw.newLine();
                        bw.flush();
                    }
                    return "starting up...";
                }
                case "backup" -> {
                    if(ServerStream.os!=null){
                        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(ServerStream.os,StandardCharsets.UTF_8));
                        bw.write(commands[0]+" "+commands[1]);
                        bw.newLine();
                        bw.flush();
                    }
                    return "backing up...";
                }
                case "help" -> {
                    return("""
                            sendGroupMsg [GroupCode] [Message]
                            sendPrivateMsg [FriendCode] [Message]
                            setAdmin [GroupCode] [MemberCode]
                            cancelAdmin [GroupCode] [MemberCode]
                            deleteFriend [FriendCode]
                            deleteGroup [GroupCode]
                            mute [GroupCode] [MemberCode] [Time(seconds)]
                            groupName [GroupCode] [NewName]
                            kick [GroupCode] [MemberCode] [Reason]
                            set [GroupCode] [FullPathToFunction]
                            set [FullPathToFunction] [Key] [Value]
                            restart [MCServerName]
                            restart
                            forceStop [MCServerName]
                            backup [MCServerName]
                            help
                            """);
                }
                default -> {
                    if(commands[0].startsWith("[")&&commands[0].contains("]/")){
                        ServerStream.os.write((cmd+"\r\n").getBytes(StandardCharsets.UTF_8));
                        return "Command Sent!";
                    }else{
                        return ("Unknown Command, Type \"help\" For Help");
                    }
                }
            }
        }catch(Exception e){
            return("Wrong Command, Type \"help\" For Help\n"+e);
        }

    }
}
