package io.github.et.tools;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;
import io.github.et.conopt4j.launcher.Launcher;
import io.github.et.conopt4j.logger.Logger;
import io.github.et.conopt4j.threading.command.Command;
import io.github.et.conopt4j.threading.command.Parameter;
import io.github.et.conopt4j.threading.command.Type;
import io.github.et.eventListener.AdminBuffet;
import io.github.et.eventListener.ChangeGroupName;
import io.github.et.eventListener.LeaverListener;
import io.github.et.eventListener.RequestPasser;
import io.github.et.games.roulette.Roulette;
import io.github.et.games.wordle.Wordle;
import io.github.et.messager.*;
import io.github.et.subprocessLoader.ServerStream;
import io.github.et.utils.json.JsonBuilder;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.ListenerHost;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CommandConsole {
    public static void registerCommands(Bot bot) {
        Command sendGroupMsg = new Command("sendGroupMsg");
        sendGroupMsg.setDescription("Send a message to a group chat")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG),new Parameter<>("Message", Type.STRING))
                    .addExecution(cxt->{
                        Group group=bot.getGroup(cxt.get("GroupCode"));
                        if(group==null){
                            return "Group not found";
                        }
                        group.sendMessage(String.valueOf(cxt.get("Message")));
                        return("Group Message Has Been Sent!");
                    })
                .build();
        Launcher.registerCommand(sendGroupMsg);
        Command sendPrivateMsg = new Command("sendPrivateMsg");
        sendPrivateMsg.setDescription("Send a message to a private chat")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("FriendID", Type.LONG),new Parameter<>("Message", Type.STRING))
                    .addExecution(cxt->{
                        Friend friend=bot.getFriend(cxt.get("FriendID"));
                        if(friend==null){
                            return "Friend not found";
                        }
                        friend.sendMessage(String.valueOf(cxt.get("Message")));
                        return("Private Message Has Been Sent!");
                    });
        Launcher.registerCommand(sendPrivateMsg);
        Command setAdmin = new Command("setAdmin");
        setAdmin.setDescription("Set the administrator permission for a member in group chat")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG),new Parameter<>("GroupMemberID", Type.LONG))
                    .addExecution(context ->{
                        Group group=bot.getGroup(context.get("GroupCode"));
                        if(group==null){
                            return "Group not found";
                        }
                        NormalMember member=group.get(context.get("GroupMemberID"));
                        if(member==null){
                            return "Member not found";
                        }
                        if(group.getOwner().getId()!=bot.getId()){
                            return "Group owner permission required";
                        }
                        member.modifyAdmin(true);
                        return context.get("GroupMemberID")+" has been set as an admin";
                    })
                .build();
        Launcher.registerCommand(setAdmin);
        Command cancelAdmin = new Command("cancelAdmin");
        cancelAdmin.setDescription("Cancel the administrator permission for a member in group chat")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG),new Parameter<>("GroupMemberID", Type.LONG))
                    .addExecution(context ->{
                        Group group=bot.getGroup(context.get("GroupCode"));
                        if(group==null){
                            return "Group not found";
                        }
                        NormalMember member=group.get(context.get("GroupMemberID"));
                        if(member==null){
                            return "Member not found";
                        }
                        if(group.getOwner().getId()!=bot.getId()){
                            return "Group owner permission required";
                        }
                        member.modifyAdmin(false);
                        return context.get("GroupMemberID")+" has been deprived of the admin permission";
                    })
                .build();
        Launcher.registerCommand(cancelAdmin);
        Command deleteFriend = new Command("deleteFriend");
        deleteFriend.setDescription("Delete a friend")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("FriendID", Type.LONG))
                    .addExecution(cxt->{
                        Friend friend=bot.getFriend(cxt.get("FriendID"));
                        if(friend==null){
                            return "Friend not found";
                        }
                        friend.delete();
                        return cxt.get("FriendID")+" is no longer your friend";
                    })
                .build();
        Launcher.registerCommand(deleteFriend);
        Command deleteGroup = new Command("deleteGroup");
        deleteGroup.setDescription("Leave a group")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG))
                    .addExecution(context ->{
                        Group group=bot.getGroup(context.get("GroupCode"));
                        if(group==null){
                            return "Group not found";
                        }
                        group.quit();
                        return "You left the group "+group.getId();
                    })
                .build();
        Launcher.registerCommand(deleteGroup);
        Command mute=new Command("mute");
        mute.setDescription("Mute a member in a group")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG), new Parameter<>("GroupMemberID", Type.LONG), new Parameter<>("Time", Type.INTEGER))
                .addExecution(cxt -> {
                    Group group = bot.getGroup(cxt.get("GroupCode"));
                    if (group == null) {
                        return "Group not found";
                    }
                    NormalMember member = group.get(cxt.get("GroupMemberID"));
                    if (member == null) {
                        return "Member not found";
                    }
                    member.mute(cxt.get("Time"));
                    return cxt.get("GroupMemberID")+" has been muted for "+member.getId()+" seconds";
                })
                .build();
        Launcher.registerCommand(mute);
        Command unmute=new Command("unmute");
        unmute.setDescription("Unmute a member in a group")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG), new Parameter<>("GroupMemberID", Type.LONG))
                    .addExecution(cxt->{
                        Group group = bot.getGroup(cxt.get("GroupCode"));
                        if (group == null) {
                            return "Group not found";
                        }
                        NormalMember member = group.get(cxt.get("GroupMemberID"));
                        if (member == null) {
                            return "Member not found";
                        }
                        member.unmute(cxt.get("Time"));
                        return cxt.get("GroupMemberID")+" has been unmuted";
                    })
                .build();
        Launcher.registerCommand(unmute);
        Command groupName = new Command("groupName");
        groupName.setDescription("Set group name")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode",Type.LONG),new Parameter<>("GroupName", Type.STRING))
                    .addExecution(ext-> {
                        Group group=bot.getGroup(ext.get("GroupCode"));
                        if(group==null){
                            return "Group not found";
                        }
                        group.setName(ext.get("GroupName"));
                        return "Group name has been modified as "+group.getName();
                    })
                .build();
        Launcher.registerCommand(groupName);
        Command kick=new Command("kick");
        kick.setDescription("Kick a member from a group");
        kick.setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG),new Parameter<>("GroupMemberID", Type.LONG))
                    .addExecution(context -> {
                        Group group = bot.getGroup(context.get("GroupCode"));
                        if(group==null){
                            return "Group not found";
                        }
                        NormalMember member=group.get(context.get("GroupMemberID"));
                        if(member==null){
                            return "Member not found";
                        }
                        member.kick("");
                        return "Kicked "+member.getId();
                    })
                .build();
        Launcher.registerCommand(kick);
        Command set=new Command("set");
        set.setDescription("Configuration settings")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("GroupCode", Type.LONG),new Parameter<>("FullPath",Type.STRING))
                    .addExecution(cxt->{
                        JSONObject jo1= Main.JSON_ALL;
                        JSONObject jo2=Main.JSON_NO_GUIDE;
                        String[] path = String.valueOf(cxt.get("FullPath")).split("\\.");
                        for (String s : path) {
                            jo1 = jo1.getJSONObject(s);
                            jo2 = jo2.getJSONObject(s);
                        }
                        String name=path[path.length-1];
                        name=name.replaceFirst(String.valueOf(name.charAt(0)),String.valueOf(name.charAt(0)).toLowerCase());
                        if(jo1.getBoolean(name)){
                            long id = cxt.get("GroupCode");
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
                            long id = cxt.get("GroupCode");
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
                        try {
                            JsonBuilder.update();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return "Setting Completed!";
                    })
                .addParameterNode(new Parameter<>("FullPath",Type.STRING),new Parameter<>("CommandName",Type.STRING),new Parameter<>("Value",Type.STRING))
                    .addExecution(context -> {
                                JSONObject jo1= Main.JSON_ALL;
                                JSONObject jo2=Main.JSON_NO_GUIDE;
                                String[] path= String.valueOf(context.get("FullPath")).split("\\.");
                                for (String s : path) {
                                    jo1 = jo1.getJSONObject(s);
                                    jo2 = jo2.getJSONObject(s);
                                }
                                String name = context.get("CommandName");
                                if(jo1.get(name) instanceof Boolean){
                                    jo1.put(name,Boolean.valueOf(context.get("Value")));
                                    jo2.put(name,Boolean.valueOf(context.get("Value")));
                                } else if (jo1.get(name) instanceof Integer) {
                                    jo1.put(name, Integer.parseInt(context.get("Value")));
                                    jo2.put(name, Integer.parseInt(context.get("Value")));
                                } else if (jo1.get(name) instanceof String) {
                                    StringBuilder value = new StringBuilder();
                                    jo1.put(name, context.get("Value"));
                                    jo2.put(name, context.get("Value"));
                                } else if (jo1.get(name) instanceof JSONArray) {
                                    JSONArray newArray = com.alibaba.fastjson2.JSON.parseArray("[" + context.get("Value") + "]");
                                    jo1.put(name, newArray);
                                    jo2.put(name, newArray);
                                }
                        try {
                            JsonBuilder.update();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return "Setting Completed!";
                    })
                .build();
        Launcher.registerCommand(set);
        Command restart=new Command("restart");
        restart.setDescription("Restart QQ bot/MC server")
                .setDeamon(true)
                .addParameterNode()
                    .addExecution(context ->  {
                        if (ServerStream.os != null) {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                            try {
                                bw.write("restart");
                                bw.newLine();
                                bw.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        return "Restarting...";
                    })
                .addParameterNode(new Parameter<>("MCServerName",Type.STRING))
                    .addExecution(cxt->{
                        if (ServerStream.os != null) {
                            try {
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                                bw.write("restart " + cxt.get("MCServerName"));
                                bw.newLine();
                                bw.flush();
                            }catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return "Restarting...";
                    })
                .build();
        Launcher.registerCommand(restart);
        Command forceStop=new Command("forceStop");
        forceStop.setDescription("Force stop MC server")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("MCServerName",Type.STRING))
                    .addExecution(cxt->{
                        if(ServerStream.os!=null) {
                            try {
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                                bw.write("forceStop " + cxt.get("MCServerName"));
                                bw.newLine();
                                bw.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return "shutting...";
                    })
                .build();
        Launcher.registerCommand(forceStop);
        Command startup=new Command("startup");
        startup.setDescription("Startup MC server")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("MCServerName",Type.STRING))
                .addExecution(cxt->{
                        if(ServerStream.os!=null) {
                            try {
                                if(ServerStream.os!=null){
                                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(ServerStream.os,StandardCharsets.UTF_8));
                                    bw.write("startup " + cxt.get("MCServerName"));
                                    bw.newLine();
                                    bw.flush();
                                }

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return "starting up...";
                })
                .build();
        Launcher.registerCommand(startup);
        Command backup=new Command("backup");
        backup.setDescription("Backup MC server")
                .setDeamon(true)
                .addParameterNode(new Parameter<>("MCServerName",Type.STRING))
                .addExecution(cxt->{
                    if(ServerStream.os!=null){
                        try {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                            bw.write("backup " + cxt.get("MCServerName"));
                            bw.newLine();
                            bw.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return "backing up...";
                })
                .build();
        Launcher.registerCommand(backup);
        Command resetListener=new Command("resetListener");
        resetListener.setDescription("When you have called \"restart\", but the bot doen't seem to reply,please try resetListener")
                .setDeamon(true)
                .addParameterNode()
                    .addExecution(cxt->{
                        Logger.info("正在注册监听器……");
                        List<Class<?>> clazz = null;
                        try {
                            clazz = io.github.et.utils.classLoader.ClassLoader.loadClasses();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        clazz.add(AdminBuffet.class);
                        clazz.add(ChangeGroupName.class);
                        clazz.add(LeaverListener.class);
                        clazz.add(RequestPasser.class);
                        clazz.add(ChangeConfigListener.class);
                        clazz.add(FreeTalk.class);
                        clazz.add(GetHelp.class);
                        clazz.add(MinecraftServer.class);
                        clazz.add(Nudger.class);
                        clazz.add(Repeater.class);
                        clazz.add(Replier.class);
                        clazz.add(ServerSearcher.class);
                        clazz.add(Roulette.class);
                        clazz.add(Wordle.class);
                        clazz.add(Interactions.class);
                        clazz.add(MessageCount.class);
                        clazz.add(BilibiliVideo.class);
                        for (Class<?> c : clazz) {
                            try {
                                var abc = c.getDeclaredConstructor().newInstance();
                                if (abc instanceof ListenerHost n) {
                                    Main.bot.getEventChannel().registerListenerHost(n);
                                }
                                Logger.info("已注册监听器" + c.getName());
                            }catch (Exception ignored){}
                        }
                        return "";
                    });
        Launcher.registerCommand(resetListener);

    }
}
